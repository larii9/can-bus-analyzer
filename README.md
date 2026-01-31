# CAN Bus Analyzer

Real-time CAN bus message visualization and analysis tool.

## Screenshots

### Live Message Stream
<img width="1506" height="760" alt="Captura de Tela 2026-01-31 às 16 07 38" src="https://github.com/user-attachments/assets/23b35ee0-73e0-47ce-8481-33e699bedb6a" />


### Filtering by ECU
<img width="1493" height="933" alt="Captura de Tela 2026-01-31 às 16 09 40" src="https://github.com/user-attachments/assets/7f31ef34-482c-4f6a-8adf-4145e82ea940" />


### Paused State
<img width="1510" height="949" alt="Captura de Tela 2026-01-31 às 16 10 00" src="https://github.com/user-attachments/assets/7a1ff64f-f034-4a69-95bf-b89acf4562b2" />


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

## Author

Larissa Oliveira - Software Engineering @ Universität Stuttgart
