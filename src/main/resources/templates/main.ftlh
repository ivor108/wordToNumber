<!DOCTYPE HTML>
<html>
<body>
    <div>
        <form action="/logout" method="post">
            <input type="submit" value="Sign Out"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </form>
    </div>

    <div>
        <form method="post" modelAttribute = "modeChoice">
            <input type="text" name="textFrom" placeholder="Введите текст">
            <input type="text" name="textTo" value = ${textTo!} readonly>
            <button type="submit">Добавить</button>
            <div>
                <input type="radio" id="modeChoice1" name="modeChoice" value="1" checked>
                <label for="modeChoice1">Number to word</label>
                <input type="radio" id="modeChoice2" name="modeChoice" value="2">
                <label for="modeChoice2">Word to number</label>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </form>
    </div>
    <div>Список переводов</div>
    <#list translations as translation>
        <div>
            <b>${translation.id}</b>
            <span>${translation.textFrom}</span>
            <i>${translation.textTo}</i>
            <strong>${translation.authorName}</strong>
        </div>
    </#list>
</body>
</html>