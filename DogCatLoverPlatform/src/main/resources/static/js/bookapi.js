var listTime = ['08:00','09:00','10:00','11:00','13:00','14:00','15:00','16:00','17:00','18:00']

async function loadTime(){
    time = null;
    var dates = document.getElementById("chooseDate").value;
    var startDate = document.getElementById("dateStart").value;
    var endDate = document.getElementById("dateEnd").value;

    var id = document.getElementById("id_blog").value;
    console.log(window.location.pathname)
    console.log(id)
    var url = 'http://localhost:8080/booking/booking-by-date-and-blog?date='+dates+"&id="+id;
    console.log(url)
    const response = await fetch(url, {
        method: 'GET',
        headers: new Headers({
        })
    });

    var chooseDateObj = new Date(dates);
    var startDateObj = new Date(startDate);
    var endDateObj = new Date(endDate);
    if (chooseDateObj >= startDateObj && chooseDateObj <= endDateObj) {
        var list = await response.json();
        console.log(list)

        var main = ''
        for (i = 0; i < listTime.length; i++) {
            var check = false;
            for (j = 0; j < list.length; j++) {
                var t = list[j].bookingTime.split(":")[0] + ":" + list[j].bookingTime.split(":")[1]
                if (t == listTime[i]) {
                    check = true;
                }
            }
            if (check == false) {
                main += `</span><span onclick="chooseTime(this,'${listTime[i]}')" class="time-item">${listTime[i]}</span>`
            } else {
                main += `</span><span class="time-item useds">${listTime[i]}</span>`
            }
        }
        document.getElementById("listTime").innerHTML = main
    }else {
        // `chooseDate` không nằm trong khoảng thời gian bắt đầu và kết thúc
        alert("The date you selected is not within the service period.");
        window.location.reload()
    }
}

var time = null;

function chooseTime(e, chooseTime){
    var list = document.getElementById("listTime");
    var le = list.getElementsByClassName("time-item").length;
    for(i=0; i<le; i++){
        list.getElementsByClassName("time-item")[i].classList.remove('active');
    }
    e.classList.add("active");
    time = chooseTime;
    document.getElementById("datecs").innerHTML = "date: "+ document.getElementById("chooseDate").value +", time: "+time
}

async function createAppointment(){
    var url = 'http://localhost:8080/booking/create-booking';
    var idBlog  = document.getElementById("id_blog").value;
    var chooseDate = document.getElementById("chooseDate").value;
    if(chooseDate == "" || chooseDate == null){
        alert("Choose date");
        return
    }if(time == null){
        alert("Choose time");
        return
    }
    var req = {
        "bookingTime":time,
        "bookingDate":chooseDate,
        "blogEntity_BookingEntity":{
            "id":idBlog
        }
    }

    const response = await fetch(url, {
        method: 'POST',
        headers: new Headers({
            'Content-Type': 'application/json'
        }),
        body: JSON.stringify(req)
    });
    if(response.status < 300){
        alert("Success!")
        window.location.reload()
    }
    if(response.status === 417 || response.status == 417){
        var logErr = await response.json();
        alert(logErr.defaultMessage);
        if(logErr.errorCode == 444 || logErr.errorCode === 444){
            window.location.href = '/index/login'
        }
    }
}