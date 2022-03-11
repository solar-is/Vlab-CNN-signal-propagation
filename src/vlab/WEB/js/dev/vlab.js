//global mutable state
var state = {
    matrices: [],
    currentSlideNumber: 0
};

//info about variant, will be filled on initialization
var generatedVariant = {
    activationFunction: "",
    subSamplingFunction: "",
    kernels: [],
    inputMatrix: []
}

function updateState(callback) {
    //todo add validation for currentSlideNumber change
    console.log("going to update old state", state)
    state = callback(state);
    console.log('updatedState', state);
    return state;
}

function rerender() {
    console.log('going to rerender slide using state', state);
    //todo extract html from state
    document.getElementById('jsLab').innerHTML = getHTML();
    bindActionListeners();
}

function getHTML() {
    //todo extract html from current state

    return `
        <div class="lab">
            <div class="lab-table">
                <div class="lab-header_text">Алгоритм последовательного распространения сигнала в свёрточной нейронной сети</div>
                <div class="header-buttons">
                    <button type="button" class="btn btn-info showReference" data-toggle="modal" data-target="#exampleModalScrollable">Справка</button>
                </div>
                <div class="graphComponent">
                    <div id="graphContainer"></div>
                </div>
                <div class="steps">
                    <div class="steps-buttons">
                        <input id="addStep" class="addStep btn btn-success" type="button" value="+"/>
                        <input type="button" class="minusStep btn btn-danger" value="-">
                    </div>
                    <div class="maxFlow">
                        <span>MSE:</span>
                        <input type='number' class='maxFlow-input' id="error" value="0"'/>
                    </div>                                                                                                                                            
                </div>
            </div> 
            <div class="lab-header">
                      <div class="modal fade" id="exampleModalScrollable" tabindex="-1" role="dialog" aria-labelledby="exampleModalScrollableTitle" aria-hidden="true">
                      <div class="modal-dialog modal-dialog-scrollable" role="document">
                        <div class="modal-content">
                          <div class="modal-header">
                            <h5 class="modal-title" id="exampleModalScrollableTitle">Справка по интерфейсу лабораторной работы</h5>
                            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                              <span aria-hidden="true">&times;</span>
                            </button>
                          </div>
                          <div class="modal-body">                                                                             
                                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.</p>

                                <p>Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. <b>Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.</b></p> 
                          </div>
                        </div>
                      </div>
                    </div>
                </div>                                                                      
        </div>`;
}

function bindActionListeners(appInstance) {
    //todo add listeners for all action types (track changing from client -> then update state -> then rerender slide)

    /*document.getElementById("error").addEventListener('change', () => {
        //updateState
        //rerender
    });*/
}

function init_lab() {
    return {
        setletiant: function (str) {
        },
        setPreviosSolution: function (str) {
        },
        setMode: function (str) {
        },

        init: function () {
            let variantJSON = document.getElementById("preGeneratedCode")
            if (variantJSON) {
                if (variantJSON.value !== "") {
                    updateState(() => {
                        console.log('beforeParse', variantJSON.value);
                        let variant = JSON.parse(variantJSON.value);

                        //fill generatedVariant
                        generatedVariant.activationFunction = variant.activationFunction;
                        generatedVariant.subSamplingFunction = variant.subSamplingFunction;
                        generatedVariant.kernels = variant.kernels;
                        generatedVariant.inputMatrix = variant.inputNode.payload.matrix;
                        console.log('generatedVariant', generatedVariant);

                        //fill global state
                        return {
                            matrices: [generatedVariant.inputMatrix],
                            currentSlideNumber: 0
                        }
                    });
                }
            }
            rerender()
        },

        getCondition: function () {
        },
        getResults: function () {
            //todo get results from state
            console.log('stateBeforeGetResults', state);
            let result = {qwe: "qwerty"};
            console.log('getResultsValue', result);
            return JSON.stringify(result);
        },
        calculateHandler: function (text, code) {
        },
    }
}

var Vlab = init_lab();