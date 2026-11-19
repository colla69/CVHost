import { readFileSync } from 'fs'
import path from 'path'
import vm from 'vm'
import { test, expect } from '@playwright/test'
import * as nav from '../../src/nav'
import { ROLES } from '../../src/components/CV/roles'
import { STEPS } from '../../src/components/CV/education'
import { LANGUAGES } from './helpers'

// Data tests: they read the content where it is written and load no page, so a
// new post, project or CV entry is checked without a change here.

const SRC = path.join(__dirname, '../../src')

const readJson = file => JSON.parse(readFileSync(path.join(SRC, file), 'utf8'))

// Imports that data() uses, given for real: ExperienceAndEducation takes its
// link labels from pageLabel(), and WorkExperience and Education return ROLES
// and STEPS. Left out, those lists would be undefined and the walk would skip
// the whole work history without a sign (PR #91 review, 2026-11-19).
const REAL_IMPORTS = { '@/nav': nav, './roles': { ROLES }, './education': { STEPS } }

// A component's COPY and data() list, read from its .vue file. Any other import
// becomes undefined: data() only needs those for values that are not text, such
// as the portrait, and the walk below skips them.
function loadComponent (file) {
  const source = readFileSync(path.join(SRC, file), 'utf8')
  const script = source.match(/<script>([\s\S]*?)<\/script>/)[1]
  const imported = {}
  const body = script.replace(/^import\s+(.+?)\s+from\s+'([^']+)'\s*$/gm, (line, what, from) => {
    for (const name of what.replace(/[{}]/g, '').split(',').map(name => name.trim()).filter(Boolean)) {
      imported[name] = REAL_IMPORTS[from] && REAL_IMPORTS[from][name]
    }
    return ''
  }).replace('export default', 'const component =')
  const { copy, component } = vm.runInNewContext(
    body + '\n;({ copy: typeof COPY === \'undefined\' ? undefined : COPY, component })', imported)
  return { copy, data: component.data ? component.data.call({}) : {} }
}

// The page's own .vue file and every component it pulls in from @/components.
function componentFiles (file, seen = new Set()) {
  if (seen.has(file)) return seen
  seen.add(file)
  const source = readFileSync(path.join(SRC, file), 'utf8')
  for (const [, child] of source.matchAll(/from '@\/(components\/[^']+)'/g)) {
    componentFiles(child + '.vue', seen)
  }
  return seen
}

const filled = value => typeof value === 'function' || (typeof value === 'string' && value.trim() !== '')

// Every { en, de, it } map below `value`, with the path it sits at. A COPY
// block is a map whose values are objects of their own: there each language
// must carry every key of en, each filled.
function gaps (value, where) {
  if (Array.isArray(value)) return value.flatMap((item, i) => gaps(item, `${where}[${i}]`))
  if (!value || typeof value !== 'object') return []
  if (!LANGUAGES.some(code => code in value)) return Object.entries(value).flatMap(([key, item]) => gaps(item, `${where}.${key}`))
  if (value.en && typeof value.en === 'object') {
    return LANGUAGES.flatMap(code => Object.keys(value.en)
      .filter(key => !value[code] || !filled(value[code][key]))
      .map(key => `${where}.${code}.${key}`))
  }
  return LANGUAGES.filter(code => !filled(value[code])).map(code => `${where}.${code}`)
}

// Fields that are always prose (frontend/CLAUDE.md). Written as a plain string,
// one would show English in every language and the walk would not see it.
const plainProse = (item, fields, where) => fields
  .filter(field => !item[field] || typeof item[field] !== 'object')
  .map(field => `${where}.${field} is not an { en, de, it } map`)

test('every note on the Notes page reads in English, German and Italian', () => {
  const news = readJson('components/news/news.json')
  expect(news.length).toBeGreaterThan(0)
  expect(news.flatMap(post => [
    ...plainProse(post, ['title', 'description_text'], `news id ${post.id}`),
    ...gaps(post, `news id ${post.id}`)
  ])).toEqual([])
})

test('every project on the Work page reads in English, German and Italian', () => {
  const projects = readJson('components/projectInfos/project_infos.json')
  expect(projects.length).toBeGreaterThan(0)
  expect(projects.flatMap(project => [
    ...plainProse(project, ['description', 'role_name'], `project id ${project.id}`),
    ...gaps(project, `project id ${project.id}`)
  ])).toEqual([])
})

for (const [page, file] of [['Experience', 'components/CV/ExperienceAndEducation.vue'], ['About', 'components/CV/AboutMe.vue']]) {
  test(`every text on the ${page} page reads in English, German and Italian`, () => {
    const files = [...componentFiles(file)]
    expect(files.length).toBeGreaterThan(1)
    const found = files.flatMap(component => {
      const { copy, data } = loadComponent(component)
      return [...gaps(copy, `${component} COPY`), ...gaps(data, `${component} data`)]
    })
    expect(found).toEqual([])
  })
}
