import { createHash } from 'crypto'

// Each checkout gets its own port, derived from its path, so that test runs in
// two worktrees at the same time never reuse each other's dev server.
const hash = createHash('sha256').update(__dirname).digest()
export const PORT = 20000 + (hash.readUInt16BE(0) % 10000)
