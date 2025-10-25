# Distributed Device Monitoring System

A Java RMI-based distributed system for monitoring and controlling multiple devices remotely. The system consists of three main components: a central registry for device management, device clients that provide remote services, and a manager application for monitoring and control.

## Features

- **Central Registry**: Manages active device registration and discovery
- **Remote Device Control**: Capture screenshots and camera photos from remote devices
- **Real-time Communication**: Chat functionality between manager and devices
- **Device Discovery**: Automatic detection and listing of active devices
- **Image Management**: Automatic saving of captured images with timestamps

## Architecture

The system uses Java RMI (Remote Method Invocation) for distributed communication:

- **Central Registry**: Centralized service registry for device management
- **Device Clients**: Remote services providing screenshot, camera, and chat capabilities
- **Manager Application**: Control interface for monitoring and interacting with devices

## Components

### 1. Central Registry (`Central/`)

- `CentralRegistry.java` - Interface defining registry operations
- `CentralRegistryImpl.java` - Implementation of the central registry
- `CentralRegistryRMI.java` - RMI server for the central registry

### 2. Device Client (`Device/`)

- `Device.java` - Interface defining device operations
- `DeviceImpl.java` - Implementation of device services
- `DeviceRMI.java` - RMI server for device services
- `libs/opencv-490.jar` - OpenCV library for camera operations

### 3. Manager Application (`Manager/`)

- `ManagerApp.java` - Main manager application with GUI interface
- `photos/` - Directory for saved camera photos
- `screenshots/` - Directory for saved screenshots

## Prerequisites

- Java Development Kit (JDK) 8 or higher
- OpenCV library (included in `Device/libs/`)
- Network connectivity between components

## Usage

### 1. Start Central Registry

```bash
cd Central
java CentralRegistryRMI
```

### 2. Start Device Client(s)

```bash
cd Device
java DeviceRMI
```

### 3. Start Manager Application

```bash
cd Manager
java ManagerApp
```

## How It Works

1. **Device Registration**: Device clients register themselves with the central registry
2. **Device Discovery**: Manager queries the central registry to get list of active devices
3. **Remote Operations**: Manager can perform operations on selected devices:
   - Capture screenshots
   - Take camera photos
   - Start chat sessions
4. **File Management**: Captured images are automatically saved with timestamps

## Technical Details

- **Communication Protocol**: Java RMI over TCP/IP
- **Image Format**: PNG images encoded in Base64 for transmission
- **Chat Protocol**: Socket-based communication on port 8080
- **Registry Port**: Default RMI registry port 1099
- **Threading**: Multi-threaded chat implementation for concurrent communication

## File Structure

```
├── Central/           # Central registry components
├── Device/           # Device client components
│   └── libs/         # OpenCV library
├── Manager/          # Manager application
│   ├── photos/       # Saved camera photos
│   └── screenshots/  # Saved screenshots
└── readme.md         # This file
```

## Security Note

This system is designed for educational and development purposes. For production use, consider implementing proper authentication, encryption, and security measures.
