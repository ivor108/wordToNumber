<#import "parts/common.ftlh" as c>
<#import "parts/login.ftlh" as l>
<@c.page>
    <div>
        <form method="post" modelAttribute = "modeChoice" modelAttribute = "languageChoice">
            <div class="form-group mb-3">
                <input type="text" class="form-control" name="textFrom" placeholder="Введите текст" value = "${textFrom!}">
            </div>

            <div class="form-group mb-3">
                <textarea class="form-control" rows="3" readonly>${textTo!}</textarea>
            </div>

            <div class="btn-group mb-3" role="group">
                <input type="radio" class="btn-check" name="languageChoice" id="languageChoice1" value="RU"
                        ${(languageChoice?has_content == false || languageChoice == "RU")?string("checked", "")}>
                <label class="btn btn-outline-primary" for="languageChoice1">Russian</label>

                <input type="radio" class="btn-check" name="languageChoice" id="languageChoice2" value="EN"
                        ${(languageChoice?has_content == true  && languageChoice == "EN")?string("checked", "")}>
                <label class="btn btn-outline-primary" for="languageChoice2">English</label>
            </div>

            <div class="btn-group mb-3" role="group">
                <input type="radio" class="btn-check" name="modeChoice" id="modeChoice1" value="1"
                        ${(modeChoice?has_content == false || modeChoice == "1")?string("checked", "")}>
                <label class="btn btn-outline-primary" for="modeChoice1">Number to word</label>

                <input type="radio" class="btn-check" name="modeChoice" id="modeChoice2" value="2"
                        ${(modeChoice?has_content == true  && modeChoice == "2")?string("checked", "")}>
                <label class="btn btn-outline-primary" for="modeChoice2">Word to number</label>
            </div>

            <div class="form-group mb-3">
                <button class="btn btn-primary" type = 'submit'>Translate</button>
            </div>

            <input type="hidden" name="_csrf" value="${_csrf.token}" />
        </form>
    </div>
</@c.page>