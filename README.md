# CIT-U Campus Navigator

![Build](https://img.shields.io/badge/build-passing-brightgreen)
![License](https://img.shields.io/badge/license-MIT-blue)

**Live Demo:** [https://campusnavigator.masurii.dev](https://campusnavigator.masurii.dev)

<img width="1600" height="900" alt="CIT-U Campus Navigator Application Preview" src="https://github.com/user-attachments/assets/d3a0e644-4010-4fc1-ba36-f4eaef81c1f8" />

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Technology Stack](#technology-stack)
- [Architecture](#architecture)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running the Application](#running-the-application)
- [Future Enhancements](#future-enhancements)
- [Team](#team)
- [Contact](#contact)

## Overview

The **CIT-U Campus Navigator** is a web application designed to simplify navigation across the Cebu Institute of Technology – University campus. It addresses common navigational challenges faced by students, visitors, and staff by providing an interactive map with real-time geolocation, building search, and informational markers.

## Features

- **Interactive Campus Map** — Detailed map showcasing all campus buildings and key locations using Leaflet.
- **Building Search** — Quickly locate specific buildings by name with dynamic search results.
- **Geolocation Support** — Identify and display the user's current location on campus for easier orientation.
- **Informative Markers** — Visual markers that display relevant building and point-of-interest details on interaction.
- **Responsive Design** — Optimized for desktops, tablets, and mobile devices.

## Technology Stack

| Layer | Technology |
|---|---|
| Frontend | [React](https://react.dev/) 18, [Leaflet](https://leafletjs.com/), [MUI](https://mui.com/) |
| Backend | [Spring Boot](https://spring.io/projects/spring-boot) 3.x, Java 17 |
| Database | [MySQL](https://www.mysql.com/) |
| ORM | [Hibernate](https://hibernate.org/) |
| Deployment | [Vercel](https://vercel.com/) (frontend), [Render](https://render.com/) (backend) |

## Architecture

```
┌─────────────┐      ┌─────────────────┐      ┌──────────┐
│   Vercel    │──────▶   Render API    │──────▶  MySQL   │
│  (React)    │      │  (Spring Boot)  │      │  (Data)  │
└─────────────┘      └─────────────────┘      └──────────┘
```

- **Frontend:** [https://campusnavigator.masurii.dev](https://campusnavigator.masurii.dev)
- **API:** [https://campusnavigator-api.onrender.com/api](https://campusnavigator-api.onrender.com/api)

## Prerequisites

- **Node.js** v18.x or later
- **npm** or **yarn**
- **JDK** 17 or later
- **Maven**
- **MySQL Server**

## Installation

1. **Clone the repository:**

   ```bash
   git clone https://github.com/MasuRii/CampusNavigator.git
   cd CampusNavigator
   ```

2. **Configure the database:**

   Create a MySQL database named `campus_navigator` and update the credentials in `src/main/resources/application.properties`:

   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/campus_navigator
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   spring.jpa.hibernate.ddl-auto=update
   ```

3. **Install frontend dependencies:**

   ```bash
   cd frontend/campusnavigator
   npm install
   cd ../..
   ```

4. **Build the backend:**

   ```bash
   mvn clean install
   ```

## Running the Application

1. **Start the backend:**

   ```bash
   mvn spring-boot:run
   ```

   The API will be available at `http://localhost:8080`.

2. **Start the frontend:**

   ```bash
   cd frontend/campusnavigator
   npm start
   ```

   The application will open at `http://localhost:3000`.

> [!NOTE]
> Update the frontend API endpoint configuration to point to `http://localhost:8080` when running locally.

## Future Enhancements

- Route planning between campus locations
- 3D mapping and visualization
- Offline map support
- Advanced search filters
- User authentication and personalized preferences

## Team

- **Math Lee L. Biacolo** — BSIT 3
- **Terence John N. Duterte** — BSIT 3
- **Christian Brent G. Alpez** — BSIT 3
- **Claive Justin J. Barrientos** — BSIT 3
- **Michael C. Gelera** — BSIT 3

## Contact

For questions or collaboration, contact the team leads:

- Math Lee L. Biacolo — [math.biacolo@example.com](mailto:math.biacolo@example.com)
- Claive Justin J. Barrientos — [claive.barrientos@example.com](mailto:claive.barrientos@example.com)

---

Happy navigating!
