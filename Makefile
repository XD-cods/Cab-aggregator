up:
	docker compose -f docker/docker-compose.yml up -d --build
up-local:
	docker compose -f docker/docker-compose-local.yml up -d
down:
	docker compose -f docker/docker-compose.yml down -v
down-local:
	docker compose -f docker/docker-compose-local.yml down
restart-local:
	docker compose -f docker/docker-compose-local.yml down && docker compose -f docker/docker-compose-local.yml up -d
