package com.example.e08employeesapp

data class EmployeeData(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val title: String,
    val department: String,
    val image: String
)
//{
//    "id": 1,
//    "firstName": "Violante",
//    "lastName": "Longhi",
//    "email": "vlonghi0@wikispaces.com",
//    "phone": "633-736-5682",
//    "title": "Structural Analysis Engineer",
//    "department": "Training",
//    "image": "https://randomuser.me/api/portraits/med/men/8.jpg"
//}


data class EmployeesResponse(val employees: List<EmployeeData>)
//{
//    "employees": [
//    {
//        "id": 1,
//        "firstName": "Violante",
//        "lastName": "Longhi",
//        "email": "vlonghi0@wikispaces.com",
//        "phone": "633-736-5682",
//        "title": "Structural Analysis Engineer",
//        "department": "Training",
//        "image": "https://randomuser.me/api/portraits/med/men/8.jpg"
//    },
//    ...,
//    {
//        "id": 200,
//        "firstName": "Godfry",
//        "lastName": "Lyffe",
//        "email": "glyffe1@cafepress.com",
//        "phone": "229-629-9209",
//        "title": "Marketing Assistant",
//        "department": "Business Development",
//        "image": "https://randomuser.me/api/portraits/med/women/93.jpg"
//    }
//    ]
//}