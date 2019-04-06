apiVersion: apps/v1
kind: Deployment
metadata:
  # Unique key of the Deployment instance
  name: {APP_NAME}
spec:
  # 3 Pods should exist at all times.
  replicas: 1
  template:
    metadata:
      labels:
        app: {APP_NAME}
    spec:
          containers:
      - name: {APP_NAME}
        # Run this image
        image: {IMAGE_URL}:{IMAGE_TAG}
        ports:
        - containerPort: 40080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: {SPRING_PROFILE}