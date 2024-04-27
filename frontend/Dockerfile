FROM node:18.20-alpine AS builder

WORKDIR /app
ENV PATH /app/node_modules/.bin:$PATH
COPY package.json .
RUN npm install
COPY . .
RUN npm run build

FROM nginx:alpine

COPY --from=builder /app/dist/ /usr/share/nginx/html/
COPY /nginx/conf.d /etc/nginx/conf.d

# Expose port 3000 for the Nginx server
EXPOSE 3000

# Start Nginx when the container runs
CMD ["nginx", "-g", "daemon off;"]