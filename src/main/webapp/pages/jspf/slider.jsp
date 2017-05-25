<%@ page contentType="text/html;charset=UTF-8" %>

<section class="slider cols col-12">
    <%--<img src="${pageContext.request.contextPath}/resources/img/slider-cap.png" alt="slider-image">--%>
    <div class="sliderbutton" id="slideleft" onclick="slideshow.move(-1)"></div>
    <div id="slider">
        <ul>
            <li><img src="${pageContext.request.contextPath}/resources/img/slider-cap.png" width="558" height="235"
                     alt="Image One"/></li>
            <li><img src="${pageContext.request.contextPath}/resources/img/slider-cap.png" width="558" height="235"
                     alt="Image Two"/></li>
            <li><img src="${pageContext.request.contextPath}/resources/img/slider-cap.png" width="558" height="235"
                     alt="Image Three"/></li>
            <li><img src="${pageContext.request.contextPath}/resources/img/slider-cap.png" width="558" height="235"
                     alt="Image Four"/></li>
        </ul>
    </div>
    <div class="sliderbutton" id="slideright" onclick="slideshow.move(1)"></div>
    <ul id="pagination" class="pagination">
        <li onclick="slideshow.pos(0)"></li>
        <li onclick="slideshow.pos(1)"></li>
        <li onclick="slideshow.pos(2)"></li>
        <li onclick="slideshow.pos(3)"></li>
    </ul>
</section>
<script type="text/javascript">
    var slideshow = new TINY.slider.slide('slideshow', {
        id: 'slider',
        auto: 4,
        resume: false,
        vertical: false,
        navid: 'pagination',
        activeclass: 'current',
        position: 0,
        rewind: false,
        elastic: true,
        left: 'slideleft',
        right: 'slideright'
    });
</script>