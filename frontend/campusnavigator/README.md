# Campus Navigator — Frontend

React frontend for the CIT-U Campus Navigator application.

## Setup

```bash
npm install
```

## Available Scripts

### `npm start`

Runs the app in development mode on [http://localhost:3000](http://localhost:3000).

### `npm run build`

Builds the app for production to the `build` folder.

### `npm run build:prod`

Production build with CI disabled:

```bash
npm run build:prod
```

## Environment Configuration

Ensure the API endpoint in your environment or configuration file points to the correct backend:

- **Local:** `http://localhost:8080`
- **Production:** `https://campusnavigator-api.onrender.com/api`

## Tech Stack

- React 18
- React Router
- Leaflet + React-Leaflet
- MUI (Material-UI)
- React-Bootstrap
- Axios
