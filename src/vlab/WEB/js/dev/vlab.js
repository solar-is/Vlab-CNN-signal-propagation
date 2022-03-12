//global mutable state
var state = {
    matrices: [],
    currentSlideNumber: 0,
    currentSlideFilledCompletely: false
};

//info about variant, will be filled on initialization
var generatedVariant = {
    activationFunction: "",
    subSamplingFunction: "",
    kernels: [],
    inputMatrix: []
}

function updateState(callback) {
    console.log("going to update old state", state)
    state = callback(state);
    console.log('updatedState', state);
    return state;
}

function rerender() {
    console.log('going to rerender slide using state', state);
    document.getElementById('jsLab').innerHTML = getHTML();
    bindActionListeners();
}

function getHTML() {
    let kernelsHTML = '';
    for (let i = 0; i < generatedVariant.kernels.length; i++) {
        let kernelHTML = '<table class="kernel">';
        let kernelTable = generatedVariant.kernels[i].matrix;
        for (let j = 0; j < kernelTable.length; j++) {
            let rowInTable = kernelTable[j];
            kernelHTML += '<tr>'
            for (let k = 0; k < rowInTable.length; k++) {
                kernelHTML += '<td>' + rowInTable[k] + '</td>';
            }
            kernelHTML += '</tr>'
        }
        kernelHTML += '</table>';
        kernelsHTML += kernelHTML;
    }

    let isPrevButtonDisabled = state.currentSlideNumber === 0;
    let isNextButtonDisabled = state.currentSlideNumber === 4 || !state.currentSlideFilledCompletely;

    return `
        <div class="lab">
            <div class="lab-table">
                <div class="lab-header_text">Алгоритм последовательного распространения сигнала в свёрточной нейронной сети</div>
                <div class="header-buttons">
                    <span class="kernel-caption">Ядра свёртки:</span>
                    ${kernelsHTML}
                    <span class="activation-func-caption">Функция активации:</span>
                    <span class="activation-func-value">${generatedVariant.activationFunction}</span>
                    <span class="subsampling-func-caption">Функция подвыбоки:</span>
                    <span class="subsampling-func-value">${generatedVariant.subSamplingFunction}</span>
                    <button type="button" class="btn btn-info showReference" data-toggle="modal" data-target="#exampleModalScrollable">Справка</button>
                </div>
                <div class="graphComponent">
                    <div id="graphContainer"></div>
                </div>
                <div class="footer">
                    <div class="next-prev-buttons">
                        <input class="prevButton btn btn-danger" type="button" value="К предыдущему слою" ${isPrevButtonDisabled ? "disabled" : ""}>
                        <input class="nextButton btn btn-success" type="button" value="К следующему слою" ${isNextButtonDisabled ? "disabled" : ""}/>
                    </div>
                    <div class="mse-value">
                        <span>MSE:</span>
                        <input type='number' class='mse-value-input' id="error" value="0"'/>
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
                            currentSlideNumber: 0,
                            currentSlideFilledCompletely: false
                        }
                    });
                }
            }
            rerender()
        },

        getCondition: function () {
        },
        getResults: function () {
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