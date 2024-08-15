API

Создание транзакций и проверка на превышения лимита
@PostMapping
"/api/transactions"
Body json
{ 
    "accountFrom": 11111112,
    "accountTo": "321435",
    "currencyShortname" : "USD",
    "sum" : 10,
    "expenseCategory" : "service"
}

Получение всех транзакций которые превысили лимит
@GetMapping
"/api/transactions/failed?account=11111112"

Добавление нового лимита
"/api/limits"
Body json
{ 
    "account": 2223123,
    "limitSum": 5000,
    "expenseCategory" : "product"
}
Можно запустить сервис через docker-compose и обязательно доступ к интернету так как используется api для получения валют
Unit тестирования я написал для service класса где считается сам лимит
