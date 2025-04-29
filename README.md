# Flip - URL Shortener

A modern URL shortener application built with Kotlin and Next.js.

## Project Structure

```
.
├── backend/           # Kotlin backend using http4k
└── frontend/          # Next.js frontend application
```

## Backend

The backend is built with Kotlin using http4k framework, featuring:
- RESTful API
- PostgreSQL database
- Swagger documentation
- CORS support
- Logging
- Docker support

### Technologies
- Kotlin
- ktor
- PostgreSQL
- Exposed ORM
- Koin for dependency injection
- Flyway for database migrations
- Swagger for API documentation

### Setup

1. Install dependencies:
```bash
cd backend
./gradlew build
```

2. Configure database:
- Create a PostgreSQL database
- Update `application.yaml` with your database credentials

3. Run the application:
```bash
./gradlew run
```

The API will be available at `http://localhost:8080`

### API Documentation
Swagger documentation is available at `http://localhost:8080/swagger-ui`

### Docker Support
To run the backend in Docker:
```bash
docker-compose up backend
```

## Frontend

The frontend is built with Next.js

### Technologies
- Next.js 14
- TypeScript
- Tailwind CSS
- Docker

### Setup

1. Install dependencies:
```bash
cd frontend
npm install
```

2. Run the development server:
```bash
npm run dev
```

The application will be available at `http://localhost:3000`

### Docker Support
To run the frontend in Docker:
```bash
docker-compose up frontend
```

## Development

### Running the Full Stack

1. Start all services:
```bash
docker-compose up
```

This will start:
- Frontend at `http://localhost:3000`
- Backend at `http://localhost:8091`
- PostgreSQL database
- Seq for logging at `http://localhost:8081`

### Environment Variables

#### Backend
- `DB_URL`: PostgreSQL connection URL
- `DB_USER`: Database username
- `DB_PASSWORD`: Database password

## API Endpoints

### URLs
- `POST /api/urls` - Create a new short URL
- `POST /api/urls/random` - Create a new short URL with random string
- `GET /api/urls/{shortUrl}` - Get URL details
- `GET /{shortUrl}` - Redirect to original URL

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.
