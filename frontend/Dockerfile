# Stage 1: Build frontend
FROM node:14 AS build
WORKDIR /frontend
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve frontend
FROM nginx:alpine
COPY --from=build /frontend/dist/frontend /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]