<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    <div>
        <@l.logout/>
        <span><a href="/user">User list</a></span>
        <span><a href="/history">History</a></span>
    </div>

    <div>
        <form method="post" modelAttribute = "modeChoice">
            <input type="text" name="textFrom" placeholder="Введите текст" value = "${textFrom!}">
            <input type="text" name="textTo" readonly value = "${textTo!}">
            <button type="submit">Добавить</button>
            <div>
                <input type="radio" id="modeChoice1" name="modeChoice" value="1" ${(modeChoice?has_content == false || modeChoice == "1")?string("checked", "")}>
                <label for="modeChoice1">Number to word</label>
                <input type="radio" id="modeChoice2" name="modeChoice" value="2" ${(modeChoice?has_content == true  && modeChoice == "2")?string("checked", "")}>
                <label for="modeChoice2">Word to number</label>
            </div>
            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </form>
    </div>
</@c.page>