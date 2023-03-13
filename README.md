# Информационный поиск #

## Задание 1 ##

Реализован алгоритм загрузки страниц с портала habr в методе `HttpCrawler.download`. Вначале извлекаем пути статей)
с помощью метода `HabrUrlBuilder.getPageUrls`, далее получаем содержимое страницы с помощью
метода `HabrUrlBuilder.getPageContent`
и выгружаем на диск методом `FileUploader.uploadFile`.

## Задание 2 ##

Реализован алгоритм токенизации и лемматизации при помощи библиотеки CoreNLP. Вначале идет токенизация
в методе ```TextTokenizer.tokenize``` и загрузка в файл при помощи ```FileUploader.uploadTokensInFile```, затем при
помощи метода ```WordLemmatizer.lemmatize``` получаем леммы и записываем их в файл методом
```FileUploader.uploadLemmasInFile```. Считывание текста из выгруженных страничек происходит в
```ParseUtils.getSourceTextFromFiles```

## Задание 3 ##

Реализован алгоритм создание инвертированного индекса в методе ```InvertedIndexFactory.buildIndex```. Вначале из
файла `lemmas.txt`
выкачиваются все леммы и токены и
помещаются в словарь - где ключом является токен, а значением - индекс (индекс включает в себя лемму и словарь [токен,
множество файлов с этим токеном]). После алгоритм проходится по исходными файлам, и кладёт в словарь напротив токена
номер документа.

Реализован алгоритм булевого поиска по файлам. В начале
в ```SimpleFileSearch.findPathByExpression``` передаётся запрос для поиска, который делегируется в
метод```BooleanExpression.resolve```, в котором запрос парсится и преобразуется в дерево выражений при помощи
библиотеки ```jbool_expressions```, далее методе ```BooleanExpression.getFilesRecursively``` происходит рекурсивный
обход дерева в глубину и склеивание вершин согласно встретившейся операции и получение итогового набора значений.

## Задание 4 ##

Реализован алгоритм TfIdf в методе ```TfIdfFactory.calculateTfidf```. Вначале переводится в словарь от токена к лемме,
затем для каждого токена подсчитывается количество вхождений в файл. Далее подсчитывается сколько раз токен встречался
в файлах и на основе этих данных расчитывались tf и idf.