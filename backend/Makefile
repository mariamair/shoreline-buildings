dev:
		docker compose -f ../docker-compose.yml -f ../docker-compose.development.yml up -d --build

dev backend:
		docker compose -f ../docker-compose.yml -f ../docker-compose.development.yml up -d --build backend

dev db:
		docker compose -f ../docker-compose.yml -f ../docker-compose.development.yml up -d --build db

dev seed:
		docker compose -f ../docker-compose.yml -f ../docker-compose.development.yml up --build seed

down:
		docker compose down

logs:
		docker compose logs -f

.PHONY: dev down logs