# Информационный поиск #
## Задание 1 ##

Реализован алгоритм загрузки страниц с портала habr в методе `HttpCrawler.download`. Вначале извлекаем пути статей)
с помощью метода `HabrUrlBuilder.getPageUrls`, далее получаем содержимое страницы с помощью метода `HabrUrlBuilder.getPageContent`
и выгружаем на диск методом `FileUploader.uploadFile`.

## Задание 2 ##

Реализован алгоритм токенизации и лемматизации при помощи библиотеки CoreNLP. Вначале идет токенизация
в методе ```TextTokenizer.tokenize``` и загрузка в файл при помощи ```FileUploader.uploadTokensInFile```, затем при
помощи метода ```WordLemmatizer.lemmatize``` получаем леммы и записываем их в файл методом
```FileUploader.uploadLemmasInFile```. Считывание текста из выгруженных страничек происходит в
```ParseUtils.getSourceTextFromFiles```