import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import fs from 'fs'
import path from 'path'

const VIDEO_DIR = '/Users/zainabfirdaus/Desktop'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    // Serve files from Desktop under the /videos/ URL prefix
    {
      name: 'video-server',
      configureServer(server) {
        server.middlewares.use('/videos', (req, res, next) => {
          const filePath = path.join(VIDEO_DIR, req.url.replace(/^\//, ''))
          if (!fs.existsSync(filePath)) {
            res.statusCode = 404
            return res.end('Not found')
          }

          const stat = fs.statSync(filePath)
          const ext  = path.extname(filePath).toLowerCase()
          const mime = ext === '.mov' ? 'video/quicktime'
                     : ext === '.mp4' ? 'video/mp4'
                     : ext === '.webm' ? 'video/webm'
                     : 'application/octet-stream'

          const range = req.headers.range
          if (range) {
            const [startStr, endStr] = range.replace(/bytes=/, '').split('-')
            const start    = parseInt(startStr, 10)
            const end      = endStr ? parseInt(endStr, 10) : stat.size - 1
            const chunkSize = end - start + 1
            res.writeHead(206, {
              'Content-Range':  `bytes ${start}-${end}/${stat.size}`,
              'Accept-Ranges':  'bytes',
              'Content-Length': chunkSize,
              'Content-Type':   mime,
            })
            fs.createReadStream(filePath, { start, end }).pipe(res)
          } else {
            res.writeHead(200, {
              'Content-Length': stat.size,
              'Content-Type':   mime,
              'Accept-Ranges':  'bytes',
            })
            fs.createReadStream(filePath).pipe(res)
          }
        })
      },
    },
  ],
})
