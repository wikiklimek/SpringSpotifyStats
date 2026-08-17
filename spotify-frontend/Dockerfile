# Etap 1: Budowanie statycznych plików Reacta
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Etap 2: Serwowanie przez Nginx
FROM nginx:alpine
COPY --from=build /app/dist /usr/share/nginx/html
# Kopiujemy naszą nową konfigurację!
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]