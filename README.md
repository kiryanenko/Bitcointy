# Bitcointy
АПИ - https://market.mashape.com/pablo-merino/bitcointy

Там есть метод GET `/average/:currency`, который возвращает текущую стоимость одного биткоина в выбранной валюте.

Activity настроек: выбор валюты (можно сделать только RUB и USD)
MainActivity: отображается текущий курс.

Все требования как и в первом РК. Значение кэшируем, чтобы пользователь видел на экране последний известный курс, 
даже если нет интернета в данный момент. Фоновое обновление делать не нужно. Обновлять курс нужно при открытии и 
разворачивании приложения, а также вручную при нажатии кнопки.
