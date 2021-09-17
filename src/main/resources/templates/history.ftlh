<#import "parts/common.ftlh" as c>

<@c.page>
    <form method="get" action="/history">
        <div class="input-group mb-3">
            <input type="text" class="form-control" name="filter" value="${filter!}">
            <button class="btn btn-outline-secondary" type="submit" >Search</button>
        </div>
    </form>
    <div class="row row-cols-1 row-cols-md-3 g-4">
        <#list translations as translation>
            <div class="card">
                <div class="card-body">
                    <h5>${translation.textFrom}</h5>
                    <p>${translation.textTo}</p>
                    <i>${translation.authorName}</i>
                </div>
            </div>
        <#else>
            <div class="m-2">
                <span>No translations</span>
            </div>
        </#list>
    </div>
</@c.page>