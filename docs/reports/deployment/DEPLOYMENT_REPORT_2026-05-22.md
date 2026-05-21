# Deployment Report — 2026-05-22

## Deployment Summary
Vercel production redeploy triggered SSL certificate provisioning for `campusnavigator.masurii.dev`, resolving the HTTP 525 error. HTTPS now serves the React SPA successfully (HTTP 200). CORS preflight and GET requests from both custom domain and Vercel preview URL pass with correct `access-control-allow-origin` headers — no Render redeploy was needed as the env var was already in effect.

## Infrastructure Changes
- **Vercel**: Production redeploy via `vercel --prod` triggered asynchronous SSL provisioning for `campusnavigator.masurii.dev`
- **Render**: No changes — CORS already includes both origins
- **render.yaml**: Already updated (prior task) with both origins — verified in effect via live CORS checks

## Configuration Verified
| Config Item | Value | Status |
|---|---|---|
| Vercel Domain | `campusnavigator.masurii.dev` on `campusnavigator` project | Added, SSL provisioned |
| HTTPS Frontend | `https://campusnavigator.masurii.dev` | HTTP 200 ✅ |
| Backend API Ref | `https://campusnavigator-api.onrender.com/api` | In JS bundle ✅ |
| CORS: Custom Domain | `https://campusnavigator.masurii.dev` | OPTIONS 200, GET 200 ✅ |
| CORS: Vercel Preview | `https://campusnavigator-indol.vercel.app` | OPTIONS 200, GET 200 ✅ |
| Backend Health | `/actuator/health` | `{"status":"UP"}` ✅ |

## Deploy Command
```bash
cd frontend/campusnavigator && vercel --prod --yes
```

## Verification Commands
```bash
# HTTPS custom domain
curl -sI https://campusnavigator.masurii.dev

# CORS preflight (both origins)
curl -sI -X OPTIONS -H "Origin: https://campusnavigator.masurii.dev" \
  -H "Access-Control-Request-Method: POST" \
  https://campusnavigator-api.onrender.com/api/user/print

curl -sI -X OPTIONS -H "Origin: https://campusnavigator-indol.vercel.app" \
  -H "Access-Control-Request-Method: POST" \
  https://campusnavigator-api.onrender.com/api/user/print

# CORS GET (both origins)
curl -sD- -H "Origin: https://campusnavigator.masurii.dev" \
  https://campusnavigator-api.onrender.com/api/user/print

curl -sD- -H "Origin: https://campusnavigator-indol.vercel.app" \
  https://campusnavigator-api.onrender.com/api/user/print
```

## Rollback
- **Vercel frontend**: Revert to previous deployment via Vercel Dashboard → Deployments → select prior deployment → "Promote to Production"
- **No backend changes deployed** — no rollback needed

## Known Assumptions
- Cloudflare SSL mode is "Full (Strict)" — after Vercel SSL provisioning, the handshake succeeded
- Vercel domain verification was already completed via the existing DNS TXT/nameserver setup
- Render CORS `CORS_ALLOWED_ORIGINS` already contains both origins from a prior deploy or manual env var update
