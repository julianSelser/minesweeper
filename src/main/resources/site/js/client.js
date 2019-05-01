export function ClientLibrary (someBasePath, someConfig) {
    let self = this;

    this.basePath = someBasePath
    this.config = Object.assign({ mode: 'cors' }, someConfig)

    this.signup = function(aUsername, aPassword) {
        return fetch(url("signup"), request("POST", {
            username: aUsername, password: aPassword
        }))
    }

    this.login = function (aUsername, aPassword) {
        let body = {username: aUsername, password: aPassword}

        return fetch(url("login"), request("POST", body)).then(success => {
            self.username = aUsername
            self.password = aPassword
        })
    }

    this.create = function (game) {
        let isGameValid = isInt(game.width) && isInt(game.height) && isInt(game.mines)

        if(!isGameValid) throw new Error("Game must contain width, height and mines number")

        return fetch("games", withAuth(request("POST", game)))
    }

    this.getGames = function (gameId) {
        return fetch(url("games", gameId), withAuth(request("GET")))
    }

    this.move = function (gameId, move, spot) {
        let isSpotValid = isInt(spot.x) && isInt(spot.y)
        let isValidMove = move == "mark" || move == "sweep"

        if(!isSpotValid || !isValidMove || !isInt(gameId))
            throw new Error("Spot should have coordinates x and y and be integers. Move is either 'mark' or 'sweep'")

        return fetch(url("games", gameId, move), withAuth(request("PUT", spot)))
    }

    ////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////PRIVATE HELPER METHODS////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////

    function isInt(value) {
        return Number.isInteger(Number(value))
    }

    function url(parts) {
        let arrParts = Array.prototype.slice.call(arguments) //javascript old baggage

        let base = self.basePath
        let builtUrl = arrParts.filter(el => el).join("/")

        return base? base + "/" + builtUrl : builtUrl
    }

    function request(method, body){
        let headers = new Headers();
        headers.set('Content-Type', 'application/json')

        return Object.assign(
            { method, headers },
            self.config,
            body && {body: JSON.stringify(body)}
        )
    }

    function withAuth(request) {
        request.headers.set('Authorization', 'Basic ' + btoa(self.username + ":" + self.password))

        return request;
    }
}