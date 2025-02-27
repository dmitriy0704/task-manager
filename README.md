# Проект Task Management System

## Запуск

```shell
   docker-compose up -d
```

> Приложение запускается на порту 8189
>

## Документация

> Ссылка на
[Swagger](http://localhost:8189/swagger-ui/index.html#/)
>
> Ссылка на [OpenApi](http://localhost:8189/v3/api-docs)
>
> openapi.json - в папке /resources/docs

## Примечания

### Безопасность

> Для регистрации используются три поля: username, email и password.
> username и email валидируются на уникальность. При успешной регистрации в ответ
> приходит токен. По условию задачи пользователь может изменять только статус 
> задачи и комментарии. Все остальные запросы выполняются от имени
> администратора. Для демонстрации работы Api добавлена возможность
> назначения текущего пользователя Администратором. 
> 
> При регистрации задачи в поле "author" привязывается пользователь создавший ее.
> Тут же в поле "executor" необходимо указать email исполнителя задачи.
> В методах обновления статуса и комментариев текущий участник с ролью USER 
> сравнивается по полю user.email с task.executor. В случае несовпадения 
> бросается исключение о том, что пользователь не является исполнителем. Или
> если он админ - можно обновлять любую задачу. Нельзя регистрировать задачи
> с одинаковыми заголовками.

## Пагинация и сортировка

> В API есть два метода с почти одинаковым действием.  /get-tasks и /filter-tasks.
> Первый выводит все записи без возможности сортировки и пагинации. 
> Второй с параметрами offset, limit и sort. Первые два необязательные. 
> Третий обязательный "из коробки". За третий в методе
> `PageRequest.of(offset, limit, Sort.by(Sort.Direction.ASC, sortField))` отвечает
> параметр `Sort.by(Sort.Direction.ASC, sortField)`. Сделать его "необязательным
> для swagger" удалось сделать добавив параметр required = false в
>  `@RequestParam(value = "sort")` но в этом случае приложение падало с ошибкой
> о том что этот параметр пуст. Имплементируется интерфейс JpaRepository, попытка
> использовать PagingAndSortingRepository не принесла результата. Погуглив, 
> нашел док про Spring Data Rest с возможностью пагинации и сортировки, 
> но данная зависимость вызвала конфликт с другими и в итоге было принято решение
> для демонстрации сделать два метода - с фильтрацией и без.

### Исключения

> Для перехвата исключений используется глобальный перехватчик
> `GlobalControllerExceptionHandler`.  
> В ответах используется ProblemDetails, приводящий сообщения к одному
> виду. Так же, для большей информативности ответов было
> принято решение использовать исключения в случаях
> возврата "пустых ответов" таких, как "Пользователь не найден",
> "Список задач пуст" и т.д. Намерено выбран статус 404, так он позволяет 
> отображать тело ответа. Знаю, что у него немного другой смысл, но 
> 204(No content) не показывает тело ответа. Решил для большей информативности
> выдавать 404.
>
> Исключения бросаются в сервисах. В контроллере задач TaskController
> намеренно не используются ResponseEntity<>. Так как в случае использования
> в виде ResponseEntity<>(CustomException) перетирается объект ответа
> ProblemDetails внутри всех кастомных исключений, переданных в глобальный
> обработчик. Сообщения выводились вместе с большим стектрейсом. Управлять этим
> было сложно и было принято решение отказаться от ResponseEntity.
>
> 