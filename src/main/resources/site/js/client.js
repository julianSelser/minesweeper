export default new ClientLibrary()

function ClientLibrary () {
    let self = this;
    this.config = { mode: 'cors' }

    this.signup = function(aUsername, aPassword) {
        return doFetch(url("signup"), request("POST", {
            username: aUsername, password: aPassword
        }))
    }

    this.login = function (aUsername, aPassword) {
        let body = {username: aUsername, password: aPassword}

        return doFetch(url("login"), request("POST", body)).then(success => {
            self.username = aUsername
            self.password = aPassword
        })
    }

    this.create = function (game) {
        let isGameValid = isInt(game.width) && isInt(game.height) && isInt(game.mines)

        if(!isGameValid) throw new Error("Game must contain width, height and mines number")

        return doFetch("games", withAuth(request("POST", game)))
    }

    this.getGames = function (gameId) {
        return doFetch(url("games", gameId), withAuth(request("GET")))
    }

    this.move = function (gameId, move, spot) {
        let isSpotValid = isInt(spot.x) && isInt(spot.y)
        let isValidMove = move == "mark" || move == "sweep"

        if(!isSpotValid || !isValidMove || !isInt(gameId))
            throw new Error("Spot should have coordinates x and y and be integers. Move is either 'mark' or 'sweep'")

        return doFetch(url("games", gameId, move), withAuth(request("PUT", spot)))
    }

    this.setBasePath = function (aPath) {
        self.basePath = aPath
    }

    this.setConf = function (aConfig) {
        self.config = Object.assign({ mode: 'cors' }, aConfig)
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

    function doFetch(url, config) {
        //WTF fetch() sucks and doesnt reject
        //promise on bad HTTP status codes :(
        return fetch(url, config).then(response => {
            if(!response.ok){
                return response.text().then(error => Promise.reject(error))
            } else{
                return response
            }
        })
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