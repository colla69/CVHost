import * as fs from 'fs'

/**
 * One DNS record, parsed from infra/zone-records.txt.
 *
 * That file is the single source of truth for the records this zone must
 * reproduce from Strato. `scripts/aws-infra.sh verify` reads the same file to
 * diff the new zone against live DNS before the nameserver cutover, so there is
 * no second list to keep in step.
 */
export interface ZoneRecord {
  /** Label relative to the zone apex. Empty string means the apex itself. */
  name: string
  type: string
  ttl: number
  values: string[]
}

/**
 * Format, one record per line, `#` starts a comment:
 *
 *   @      A      3600   85.214.103.161
 *   *      CNAME  3600   colarietitosti.info.
 *   @      MX     3600   10 mail.example.net.;20 mail2.example.net.
 *
 * Multiple values for one name+type are separated by `;`.
 */
export function readZoneRecords (filePath: string): ZoneRecord[] {
  const text = fs.readFileSync(filePath, 'utf8')
  const records: ZoneRecord[] = []

  text.split('\n').forEach((rawLine, index) => {
    const line = rawLine.split('#')[0].trim()
    if (line === '') return

    const parts = line.split(/\s+/)
    if (parts.length < 4) {
      throw new Error(
        `zone-records.txt:${index + 1}: expected "name TYPE TTL value", got: ${line}`
      )
    }

    const [rawName, rawType, rawTtl] = parts
    const value = parts.slice(3).join(' ')

    const ttl = Number(rawTtl)
    if (!Number.isInteger(ttl) || ttl <= 0) {
      throw new Error(`zone-records.txt:${index + 1}: TTL must be a positive integer, got: ${rawTtl}`)
    }

    records.push({
      name: rawName === '@' ? '' : rawName,
      type: rawType.toUpperCase(),
      ttl,
      values: value.split(';').map(v => v.trim()).filter(v => v !== '')
    })
  })

  return records
}
