# Agrosoft

Pseudo-Dokumentacja API. 

WIP

---

## Endpoint

**auth/signin**

### Metoda: 

*POST*

### Parametry 
```json
{
  "username": "abcd@xyz.me",
  "password": "mleko"
}
```

### Odpowiedź 

Dla poprawnych danych logowania:

*Status 200*

```json
{
   "accessToken": "token",
    "tokenType": "Bearer"
}
```
Dla niepoprwanych danych logowania:  

*Status 401*

```json
{
    "timestamp": "2019-05-15T20:29:53.477+0000",
    "status": 401,
    "error": "Unauthorized",
    "message": "Bad credentials",
    "path": "/api/auth/signin"
}
```

---
## Endpoint

**auth/signup**

### Metoda: 

*POST*

### Parametry 
```json
{
  "first_name": "Jan",
  "last_name": "Kowalski",
  "username": "abcd@xyz.me",
  "password": "mleko"
}
```

W odpowiedzi przychodzi

*Status 201*

```json
{
    "success": true,
    "message": "User registered successfully"
}
```
---

## Endpoint 

**users**

### Metoda

*GET*

W odpowiedzi przychodzi

*Status 200*

```json
[
    {
        "user_id": 1,
        "first_name": "Janusz",
        "last_name": "Siemafor",
        "username": "janusz123656@ziemniaczki.ppl",
        "created_at": "2019-04-21T13:42:33Z",
        "farm": {
            "farm_id": 1,
            "address": "Ziemniakowa 21, Zielona Gora"
        },
        "roles": [
            {
                "role_id": 2,
                "name": "ROLE_BOSS"
            }
        ]
    },
    {
        "user_id": 2,
        "first_name": "Janusz",
        "last_name": "Siemafor",
        "username": "jan3123usz@ziemniaczki.ppl",
        "created_at": "2019-04-23T20:47:37Z",
        "farm": {
            "farm_id": 1,
            "address": "Ziemniakowa 21, Zielona Gora"
        },
        "roles": [
            {
                "role_id": 1,
                "name": "ROLE_USER"
            }
        ]
    },
    {
        "user_id": 3,
        "first_name": "Janusz",
        "last_name": "Szef",
        "username": "janusz@szef.pl",
        "created_at": "2019-04-24T13:32:51Z",
        "farm": {
            "farm_id": 1,
            "address": "Ziemniakowa 21, Zielona Gora"
        },
        "roles": [
            {
                "role_id": 1,
                "name": "ROLE_USER"
            },
            {
                "role_id": 2,
                "name": "ROLE_BOSS"
            }
        ]
    }
]
```
---

## Endpoint 

**users/me**

**users/{id}**

### Metoda

*GET*

W odpowiedzi przychodzi aktualnie zalogowany użytkownik lub użytkownik o zadanym id

*Status 200*

```json
{
    "user_id": 3,
    "first_name": "Janusz",
    "last_name": "Szef",
    "username": "janusz@szef.pl",
    "farm": {
        "farm_id": 1,
        "address": "Ziemniakowa 21, Zielona Gora"
    },
    "authorities": [
        {
            "authority": "ROLE_USER"
        },
        {
            "authority": "ROLE_BOSS"
        }
    ],
    "enabled": true,
    "accountNonExpired": true,
    "accountNonLocked": true,
    "credentialsNonExpired": true
}
```
---

## Endpoint 

**users/{id}**

### Metoda

*PATCH*

*DELETE*

Tylko pan szef może robić takie rzeczy, zwykły użytkownik nie ma uprawnień.

Dla Patcha zawraca *status 200* i jsona z aktualizowanym użytkownikiem.
Patchować można `first_name`, `last_name`, `username`.

Jeżeli chce się patchować/usunąć użytkownika który nie istnieje to 404 na twarz.

Dla poprawnie usuniętego użytkownika zwraca *status 200*.

---

## Endpoint

**messages**

### Metoda

*GET*

Pobiera tablicę wszystkie wiadomości które dostał aktualnie zalogowany użytkownik.

### Metoda 

*POST*

Pozwala wysłać wiadomość do innego użytkownika.

```json
{
  "recipient_id": 1,
  "subject": "temat",
  "body": "tresc"
}
```

---

## Endpoint

**messages/{id}**

### Metoda

*GET*

Pobiera pojedynczą wiadomość po id.

### Metoda 

*DELETE*

Usuwa wiadomość. (zwraca *status 201* jak usunie (?))

---

Dla Field, Machine i Silo endpointy są praktycznie takie same. 

**silos**

  Patchować można: `content` i `fill_level`
  
  Dla posta

  ```json
    {
      "content": "pusto",
      "capacity": 1000,
      "fill_level": 20.0
    }
  ```

**fields**

  Patchować można: `crop` i `status`

   Dla posta

  ```json
    {
      "area": 10.0,
      "crop": "Ziemniaczki",
      "status": "rośnie"
    }
  ```

**machines**

  Patchować można: `monthly_instalment`

  Dla posta

  ```json
    {
      "brand": "Ursus",
      "model": "C-360",
      "monthly_instalment": 10.0
    }
  ```

  Dla *GET* pobierze listę silosów/maszyn/pól.
  Można odpytać o konkretny silos/maszynę/pole podając id **machines/id** **fields/id** **silos/id**

  Dla *DELETE* zwracane jest 201 lub 404 jeżeli próbuje usnąć się coś co nie istnieje / należy dla innej farmy.
