# Информационный поиск #
## Задание 1 ##

Реализован алгоритм загрузки страниц с портала habr в методе `HttpCrawler.download`. Вначале извлекаем пути статей)
с помощью метода `HabrUrlBuilder.getPageUrls`, далее получаем содержимое страницы с помощью метода `HabrUrlBuilder.getPageContent`
и выгружаем на диск методом `FileUploader.uploadFile`.