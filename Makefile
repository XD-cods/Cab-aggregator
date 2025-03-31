up:
	docker-compose -f docker/docker-compose.yml up -d --build
up-local:
	docker-compose -f docker/docker-compose-local.yml up -d --build
down:
	docker-compose -f docker/docker-compose.yml down && docker volume rm cab-aggregator_kafka-data
down-local:
	docker-compose -f docker/docker-compose-local.yml down
