# CAN Bus Analyzer

Real-time CAN bus message visualization and analysis tool.

## Tech Stack

- **Backend:** Java 17, Spring Boot 3, WebSocket
- **Frontend:** Vue 3, Vite, TailwindCSS

## Quick Start

### Backend
```bash
cd backend
mvn spring-boot:run
```

### Frontend
```bash
cd frontend
npm install
npm run dev
```

Open http://localhost:5173

## Simulated ECUs

| ID Range | ECU |
|----------|-----|
| 0x100-0x1FF | Engine Control |
| 0x200-0x2FF | Transmission |
| 0x300-0x3FF | ABS/ESP |
| 0x400-0x4FF | Instrument Cluster |
| 0x500-0x5FF | Body Control |

## API

| Endpoint | Description |
|----------|-------------|
| `WS /ws/can` | Real-time CAN messages |
| `GET /api/ecus` | List of ECUs |
| `GET /api/stats` | Message statistics |

## Author

Larissa Oliveira - Software Engineering @ Universität Stuttgart
