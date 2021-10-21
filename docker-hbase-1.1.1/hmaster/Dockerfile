FROM brijeshdhaker/hbase-base:1.1.1

COPY docker-hbase-1.1.1/hmaster/run.sh /run.sh
RUN chmod +x /run.sh

EXPOSE 16000 16010

CMD ["/run.sh"]