up:
	docker-compose -f docker/docker-compose.yml up -d --build
up-local:
	docker-compose -f docker/docker-compose-local.yml up -d --build
down:
	docker-compose -f docker/docker-compose.yml down && docker volume rm cab-aggregator_kafka1-data && \
  docker volume rm cab-aggregator_kafka2-data && \
  docker volume rm cab-aggregator_kafka3-data
down-local:
	docker-compose -f docker/docker-compose-local.yml down
