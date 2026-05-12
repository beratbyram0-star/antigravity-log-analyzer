<p align="center">
  <img src="https://img.shields.io/badge/Java-26-ED8B00?style=flat-square&logo=openjdk&logoColor=white" />
  <img src="https://img.shields.io/badge/Spring%20Boot-3.4.2-6DB33F?style=flat-square&logo=springboot&logoColor=white" />
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=flat-square&logo=postgresql&logoColor=white" />
  <img src="https://img.shields.io/badge/Docker-Compose-2496ED?style=flat-square&logo=docker&logoColor=white" />
  <img src="https://img.shields.io/badge/Tailwind%20CSS-3-06B6D4?style=flat-square&logo=tailwindcss&logoColor=white" />
</p>

---

# Antigravity Log Analyzer

> **High-performance real-time log ingestion and heuristic parsing engine.**

A production-grade log management infrastructure engineered to ingest, decompose, and visualize high-frequency system events. Raw unstructured data enters the pipeline; structured, queryable insight comes out — in milliseconds.

---

## Architecture

```
┌─────────────────────────────────────────────────────────────────────┐
│                        Antigravity Pipeline                        │
│                                                                     │
│   ┌──────────┐    ┌──────────────┐    ┌───────────┐    ┌────────┐  │
│   │ Raw Data │───>│ Regex Engine │───>│ JPA Layer │───>│ Postgre│  │
│   │ (Input)  │    │ (Heuristic   │    │ (Hibernate│    │   SQL  │  │
│   │          │    │  Parsing)    │    │  6 + ORM) │    │  16    │  │
│   └──────────┘    └──────────────┘    └───────────┘    └────┬───┘  │
│                                                              │      │
│                         ┌────────────────┐                   │      │
│                         │  Dashboard UI  │<──────────────────┘      │
│                         │  (REST API +   │                          │
│                         │   Tailwind)    │                          │
│                         └────────────────┘                          │
└─────────────────────────────────────────────────────────────────────┘
```

**Flow:** Unstructured log data is received via REST endpoints → the heuristic regex engine extracts severity level, message body, and metadata → the JPA persistence layer maps entities to PostgreSQL → the UI renders real-time state through the API.

---

## Key Features

### Heuristic Regex Parsing Engine

Not a simple string split. The parsing layer applies pattern-matching heuristics to decompose raw log strings into structured fields. `[ERROR] Connection timeout on node-7` yields a typed `LogEntry` with discrete `level`, `message`, and `systemName` attributes — no manual mapping required.

### Asynchronous-Ready Architecture

The service layer is designed with a clean separation of concerns, making it trivially extensible to asynchronous processing with `@Async`, message queues, or event-driven patterns. The current synchronous path is optimized for low-latency single-request throughput.

### Optimized Database Strategy

Hibernate 6 manages the ORM layer with `ddl-auto: update` for rapid iteration. The entity model is designed with indexed-friendly column types and appropriate constraints — ready for custom index definitions as query patterns emerge at scale.

### Full-Stack Cohesion

Spring Boot 3.4 orchestrates the API layer, PostgreSQL 16 provides durable ACID-compliant persistence, and Tailwind CSS powers a minimalist, user-centric dashboard — all unified in a single deployable artifact with zero external configuration.

### Containerized Infrastructure

One command. No dependency conflicts, no version mismatches. `docker-compose up -d` provisions the entire data tier and the system is operational.

---

## Tech Stack

| Layer | Technology |
|---|---|
| **Language** | Java 26 (OpenJDK) |
| **Framework** | Spring Boot 3.4.2, Spring Web, Spring Data JPA |
| **Database** | PostgreSQL 16 (Alpine) |
| **ORM** | Hibernate 6 + Lombok |
| **Frontend** | Tailwind CSS 3, Vanilla JavaScript |
| **Infrastructure** | Docker Compose |
| **Build** | Maven + Maven Wrapper |

---

## Project Structure

```
antigravity-log-analyzer/
├── src/main/java/com/antigravity/loganalyzer/
│   ├── AntigravityLogAnalyzerApplication.java    # Entry point
│   ├── entity/
│   │   └── LogEntry.java                         # JPA entity
│   ├── repository/
│   │   └── LogRepository.java                    # Data access layer
│   ├── service/
│   │   └── LogService.java                       # Business logic + parsing engine
│   └── controller/
│       └── LogController.java                    # REST API endpoints
├── src/main/resources/
│   ├── application.properties                    # Configuration
│   └── static/
│       └── index.html                            # Dashboard UI
├── docker-compose.yml                            # PostgreSQL container
├── pom.xml                                       # Maven dependencies
└── mvnw / mvnw.cmd                              # Maven Wrapper
```

---

## Installation

### Prerequisites

- **Docker** & **Docker Compose**
- **Java 21+** (JDK)

### 1. Clone the Repository

```bash
git clone https://github.com/<your-username>/antigravity-log-analyzer.git
cd antigravity-log-analyzer
```

### 2. Provision the Database

```bash
docker-compose up -d
```

This provisions a PostgreSQL 16 instance on **port 5433**:

| Parameter | Value |
|---|---|
| Database | `loganalyzerdb` |
| Username | `loguser` |
| Password | `logpassword` |
| Port | `5433` |

### 3. Build & Launch

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

The application binds to **port 8082**.

### 4. Access the Dashboard

```
http://localhost:8082
```

---

## API Reference

| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/logs` | Retrieve all log entries |
| `POST` | `/api/logs` | Persist a structured log entry |
| `POST` | `/api/logs/parse` | Ingest a raw message, parse, and persist |

### Usage Examples

**Persist a structured log:**

```bash
curl -X POST http://localhost:8082/api/logs \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Disk usage critical",
    "level": "ERROR",
    "systemName": "server-01"
  }'
```

**Ingest and parse a raw message:**

```bash
curl -X POST http://localhost:8082/api/logs/parse \
  -H "Content-Type: application/json" \
  -d '{"rawMessage": "[WARN] Low battery detected"}'
```

**Query all logs:**

```bash
curl http://localhost:8082/api/logs
```

---

## Configuration

All runtime settings are defined in `src/main/resources/application.properties`:

| Property | Default | Description |
|---|---|---|
| `server.port` | `8082` | Application bind port |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5433/loganalyzerdb` | JDBC connection URL |
| `spring.jpa.hibernate.ddl-auto` | `update` | Schema evolution strategy |
| `spring.jpa.show-sql` | `true` | SQL query logging |

---

<br/>

<h2 align="center">📄 Yerel Dokümantasyon (Local Documentation)</h2>

---

# Antigravity Log Analyzer

> **Yüksek performanslı, gerçek zamanlı log işleme ve sezgisel ayrıştırma motoru.**

Ham yapısız veriyi alıp, yapısal ve sorgulanabilir içgörüye dönüştüren, üretim seviyesinde bir log yönetim altyapısı. Veri pipeline'a girer; milisaniyeler içinde analiz edilmiş şekilde çıkar.

---

## Mimari

```
┌─────────────────────────────────────────────────────────────────────┐
│                      Antigravity Pipeline                          │
│                                                                     │
│   ┌──────────┐    ┌──────────────┐    ┌───────────┐    ┌────────┐  │
│   │ Ham Veri │───>│ Regex Motoru │───>│ JPA       │───>│ Postgre│  │
│   │ (Girdi)  │    │ (Sezgisel    │    │ Katmanı   │    │   SQL  │  │
│   │          │    │  Ayrıştırma) │    │ (Hibernate│    │  16    │  │
│   └──────────┘    └──────────────┘    │  6 + ORM) │    └────┬───┘  │
│                                        └───────────┘         │      │
│                         ┌────────────────┐                   │      │
│                         │  Dashboard UI  │<──────────────────┘      │
│                         │  (REST API +   │                          │
│                         │   Tailwind)    │                          │
│                         └────────────────┘                          │
└─────────────────────────────────────────────────────────────────────┘
```

**Akış:** Yapısız log verisi REST endpoint'leri üzerinden alınır → sezgisel regex motoru seviye, mesaj ve metadata bilgisini çıkarır → JPA katmanı entity'leri PostgreSQL'e eşler → Dashboard, API üzerinden gerçek zamanlı durumu görselleştirir.

---

## Özellikler

### Sezgisel Regex Ayrıştırma Motoru

Basit bir string bölme işlemi değil. Ayrıştırma katmanı, ham log string'lerini yapısal alanlara ayrıştırmak için örüntü eşleme sezgiselleri uygular. `[ERROR] Connection timeout on node-7` girdisi; ayrık `level`, `message` ve `systemName` niteliklerine sahip tiplenmiş bir `LogEntry` üretir — manuel eşleme gerekmez.

### Asenkron Hazır Mimari

Servis katmanı, temiz bir sorumluluk ayrımı ile tasarlanmıştır; `@Async`, mesaj kuyrukları veya olay tabanlı örüntülerle asenkron işlemeye genişletilmesi önemsiz derecede kolaydır. Mevcut senkron yol, düşük gecikmeli tekil istek verimi için optimize edilmiştir.

### Optimize Edilmiş Veritabanı Stratejisi

Hibernate 6, hızlı iterasyon için `ddl-auto: update` ile ORM katmanını yönetir. Entity modeli, indeks dostu sütun tipleri ve uygun kısıtlamalarla tasarlanmıştır — sorgu örüntüleri ölçekte belirginleştikçe özel indeks tanımlarına hazırdır.

### Uçtan Uca Bütünlük

Spring Boot 3.4 API katmanını yönetir, PostgreSQL 16 dayanıklı ve ACID uyumlu kalıcılık sağlar, Tailwind CSS minimalist ve kullanıcı odaklı dashboard'u destekler — tamamı sıfır harici konfigürasyonla tek bir dağıtılabilir artefaktta birleştirilmiştir.

### Konteynerleştirilmiş Altyapı

Tek komut. Bağımlılık çakışması yok, sürüm uyumsuzluğu yok. `docker-compose up -d` tüm veri katmanını hazırlar ve sistem operasyonel hale gelir.

---

## Teknoloji Yığını

| Katman | Teknoloji |
|---|---|
| **Dil** | Java 26 (OpenJDK) |
| **Framework** | Spring Boot 3.4.2, Spring Web, Spring Data JPA |
| **Veritabanı** | PostgreSQL 16 (Alpine) |
| **ORM** | Hibernate 6 + Lombok |
| **Ön Yüz** | Tailwind CSS 3, Vanilla JavaScript |
| **Altyapı** | Docker Compose |
| **Derleme** | Maven + Maven Wrapper |

---

## Proje Yapısı

```
antigravity-log-analyzer/
├── src/main/java/com/antigravity/loganalyzer/
│   ├── AntigravityLogAnalyzerApplication.java    # Giriş noktası
│   ├── entity/
│   │   └── LogEntry.java                         # JPA varlığı
│   ├── repository/
│   │   └── LogRepository.java                    # Veri erişim katmanı
│   ├── service/
│   │   └── LogService.java                       # İş mantığı + ayrıştırma motoru
│   └── controller/
│       └── LogController.java                    # REST API endpoint'leri
├── src/main/resources/
│   ├── application.properties                    # Konfigürasyon
│   └── static/
│       └── index.html                            # Dashboard arayüzü
├── docker-compose.yml                            # PostgreSQL konteyneri
├── pom.xml                                       # Maven bağımlılıkları
└── mvnw / mvnw.cmd                              # Maven Wrapper
```

---

## Kurulum

### Ön Gereksinimler

- **Docker** & **Docker Compose**
- **Java 21+** (JDK)

### 1. Depoyu Klonla

```bash
git clone https://github.com/<your-username>/antigravity-log-analyzer.git
cd antigravity-log-analyzer
```

### 2. Veritabanını Hazırla

```bash
docker-compose up -d
```

Bu komut **5433 portunda** bir PostgreSQL 16 instance'ı hazırlar:

| Parametre | Değer |
|---|---|
| Veritabanı | `loganalyzerdb` |
| Kullanıcı Adı | `loguser` |
| Şifre | `logpassword` |
| Port | `5433` |

### 3. Derle ve Başlat

```bash
# Windows
.\mvnw.cmd spring-boot:run

# Linux / macOS
./mvnw spring-boot:run
```

Uygulama **8082 portuna** bağlanır.

### 4. Dashboard'a Eriş

```
http://localhost:8082
```

---

## API Referansı

| Metot | Endpoint | Açıklama |
|---|---|---|
| `GET` | `/api/logs` | Tüm log kayıtlarını getir |
| `POST` | `/api/logs` | Yapısal bir log kaydını kalıcı olarak sakla |
| `POST` | `/api/logs/parse` | Raw mesajı al, ayrıştır ve sakla |

### Kullanım Örnekleri

**Yapısal log kaydet:**

```bash
curl -X POST http://localhost:8082/api/logs \
  -H "Content-Type: application/json" \
  -d '{
    "message": "Disk kullanımı kritik seviyede",
    "level": "ERROR",
    "systemName": "server-01"
  }'
```

**Raw mesajı ayrıştır ve kaydet:**

```bash
curl -X POST http://localhost:8082/api/logs/parse \
  -H "Content-Type: application/json" \
  -d '{"rawMessage": "[WARN] Düşük batarya tespit edildi"}'
```

**Tüm logları sorgula:**

```bash
curl http://localhost:8082/api/logs
```

---

## Konfigürasyon

Tüm çalışma zamanı ayarları `src/main/resources/application.properties` dosyasında tanımlıdır:

| Özellik | Varsayılan | Açıklama |
|---|---|---|
| `server.port` | `8082` | Uygulama bağlantı portu |
| `spring.datasource.url` | `jdbc:postgresql://localhost:5433/loganalyzerdb` | JDBC bağlantı URL'i |
| `spring.jpa.hibernate.ddl-auto` | `update` | Şema evrim stratejisi |
| `spring.jpa.show-sql` | `true` | SQL sorgu loglama |

---

<br/>





