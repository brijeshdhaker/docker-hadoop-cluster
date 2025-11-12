###
sudo apt install apache2
sudo apt install php libapache2-mod-php

sudo apt install php-cli \
                 php-json \
                 php-mbstring \
                 php-xml \
                 php-pcov \
                 php-xdebug \
                 php-mysql \
                 pdo \
                 pdo_mysql

sudo systemctl restart apache2.service
sudo systemctl status apache2.service

sudo systemctl disable apache2.service
sudo systemctl stop apache2.service