# build environment


# production environment
FROM nginx:1.16.0-alpine
COPY target/dist/ /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]