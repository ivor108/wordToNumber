<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" action="/history">
        <input type="text" name="filter" value="${filter!}">
        <button type="submit">Search</button>
    </form>
    <div>Список переводов</div>
    <#list translations as translation>
        <div>
            <b>${translation.id}</b>
            <span>${translation.textFrom}</span>
            <i>${translation.textTo}</i>
            <strong>${translation.authorName}</strong>
        </div>
    </#list>
</@c.page>