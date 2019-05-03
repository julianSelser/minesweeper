import client from '/site/js/client.js'

$("#grid").delegate(".cell", "mouseup", (e) => {
    let gameId = Number($(e.delegateTarget).attr("data-game-id"))
    let spot = JSON.parse(unescape($(e.currentTarget).attr("data-pos")))
    let markOrSweep = e.which == 3 ? "mark" : "sweep"

    client
        .move(gameId, markOrSweep, spot)
        .then(fillGrid)
})

$("#newgame").click(e => {
    let game = readGameParams()

    client
        .create(game)
        .then(fillGrid)
        .then(loadGames)
})

$("#gameslist").delegate(".mybutton.gameitem", "click", e => {
    let gameId = $(e.target).attr("data-id")

    client.getGames(gameId).then(fillGrid)
})

export function loadGames() {
    return client
        .getGames()
        .then(games => {
            return games.map(game => {
                let grid = game.grid
                let template =
                    `<button data-id="{{gameId}}" class="mybutton gameitem">
                        width: {{width}} height: {{height}} mines: {{mines}} status: {{status}}
                    </button>`

                return template
                    .replace("{{gameId}}", game.id)
                    .replace("{{width}}", grid.width)
                    .replace("{{height}}", grid.height)
                    .replace("{{mines}}", grid.mines)
                    .replace("{{status}}", grid.status)
            })
        }).then(elements => {
            if(elements.length > 0){
                let gamesHTML = elements.reverse().join("")
                $("#gameslist").html(gamesHTML).show()
            }
        })
}

function readGameParams() {
    let width = Number($("#width").val())
    let height = Number($("#height").val())
    let mines = Number($("#mines").val())

    return { width, height, mines }
}

function buildGameElem(gameElem) {
    switch(gameElem) {
        case 'Hidden':      return ''
        case 'Empty':       return '<div class="empty"></div>'
        case 'Mine':        return '<i class="fas fa-bomb"></i>'
        case 'MarkedMine':  return '<i class="fas fa-flag"></i>'
        default:            return '<span class="number">{{number}}</span>'.replace("{{number}}", gameElem)
    }
}

function fillGrid(game) {
    let actualGrid = game.grid.grid

    let rows = actualGrid.map((row, y) => {
        let rowTemplate = '<div class="row">{{cells}}</div>'
        let cellTemplate = '<div class="cell" data-pos="{{spot}}">{{gameElement}}</div>'

        let cells = row.map(buildGameElem).map((built, x) => {
            return cellTemplate
                .replace("{{spot}}", escape(JSON.stringify({x, y})))
                .replace("{{gameElement}}", built)
        }).join("")

        return rowTemplate.replace("{{cells}}", cells)
    })

    $("#grid")
        .attr("data-game-id", game.id)
        .html(rows.join(""))
        .css("visibility", "visible")

    detectWinCondition(game)
}

function detectWinCondition(game) {
    var endgameColor

    if(game.grid.status == "OnGoing"){
        $("#grid").css("box-shadow", "").css("background-color", "")

        return
    } else if(game.grid.status == "Won"){
        endgameColor = "green"
    } else if(game.grid.status == "Lost"){
        endgameColor = "red"
    }

    loadGames()

    $("#grid")
        .css("box-shadow", "0px 0px 4px 5px " + endgameColor)
        .css("background-color", endgameColor)
}