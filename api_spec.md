# Meeting endpoint /meeting
## User Management is not supported yet

DB Migration -> Konsti

#Req-Body POST /meeting json

{
  title: "string",
  description: "string", # optional
  links: "string", # optional
  date_from: "string (dd-mm-yyyy:hh:mm)",
  date_until: "string (dd-mm-yyyy:hh:mm)",
  repeat: "enum (daily, weekly, monthly, never)", # std: never
  place: "string" # optional
}

Return: CREATED, BAD_REQUEST

#Req-Body PUT /meeting URI Params : Optional
  title: "string",
  description: "string", # optional
  links: "string", # optional
  date_from: "string (dd-mm-yyyy:hh:mm)",
  date_until: "string (dd-mm-yyyy:hh:mm)",
  repeat: "enum (daily, weekly, monthly, never)", # std: never
  place: "string"

Return: OK, BAD_REQUEST, NOT_FOUND

#GET /meeting URI Param: Optional
meeting-id
return: NOT_FOUND, JSON
JSON: 
  {
    title: "string",
    description: "string", # optional
    links: "string", # optional
    date_from: "string (dd-mm-yyyy:hh:mm)",
    date_until: "string (dd-mm-yyyy:hh:mm)",
    repeat: "enum (daily, weekly, monthly, never)", # std: never
    place: "string" # optional
  }

Meeting-Entity: 
  Long id,
  String title,
  String description,
  String links,
  LocalDate date_from,
  LocalDate date_until,
  Repeatable(enum) repeat,
  String place,
  Optionial<List<UserEntity>> users
