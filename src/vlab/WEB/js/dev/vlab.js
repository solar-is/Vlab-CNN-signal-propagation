let matrixIdCounter = 0;

//global mutable state
var state = {
    matrices: [],
    currentSlideNumber: 0,
    currentSlideIsEmpty: true,
    mse: 0
};

//info about generated variant, will be filled only once on initialization
var generatedVariant = {
    inputMatrix: [],
    kernels: [],
    activationFunction: "",
    subSamplingFunction: ""
}

function updateState(callback) {
    console.log("going to update old state", state)
    state = callback(state);
    console.log('updatedState', state);
    return state;
}

function linkLeftMatricesToRight(leftSideMatrices, rightSideMatrices) {
    //todo draw links from left to right
}

function makeRightMatricesEditable(rightMatrices) {
    for (let i = 0; i < rightMatrices.length; i++) {
        var table = rightMatrices.item(i);
        var cells = table.getElementsByTagName('td');

        for (let j = 0; j < cells.length; j++) {
            cells[j].onclick = function () {
                if (this.hasAttribute('data-clicked')) {
                    return;
                }

                this.setAttribute('data-clicked', 'yes')
                this.setAttribute('data-text', this.innerHTML)

                var input = document.createElement('input');
                input.setAttribute('type', 'number')
                input.value = this.innerHTML;
                input.style.width = this.offsetWidth - (this.clientLeft * 2) + "px"
                input.style.height = this.offsetHeight - (this.clientTop * 2) + "px"
                input.style.border = '0px'
                input.style.fontFamily = 'inherit'
                input.style.fontSize = 'inherit'
                input.style.textAlign = 'inherit'
                input.style.backgroundColor = "LightGoldenRodYellow"

                input.onblur = function () {
                    var td = input.parentElement
                    var orig_text = input.parentElement.getAttribute('data-text')
                    var current_text = this.value

                    if (orig_text !== current_text) {
                        //todo update global state!
                        td.removeAttribute('data-clicked')
                        td.removeAttribute('data-text')
                        td.innerHTML = current_text
                        td.style.cssText = 'padding: 5px'
                        console.log(orig_text + ' is changed to ' + current_text)
                    } else {
                        td.removeAttribute('data-clicked')
                        td.removeAttribute('data-text')
                        td.innerHTML = orig_text
                        td.style.cssText = 'padding: 5px'
                        console.log('no changes')
                    }
                }

                input.onkeypress = function () {
                    if (event.keyCode === 13) {
                        this.blur()
                    }
                }

                this.innerHTML = '';
                this.style.cssText = 'padding: 0px 0px'
                this.append(input)
                this.firstElementChild.select()
            }
        }
    }
}

function rerender() {
    console.log('going to rerender slide using state', state);
    document.getElementById('jsLab').innerHTML = getHTML();

    var rightSideMatrices = document.getElementsByClassName('right-side-matrix')
    var leftSideMatrices = document.getElementsByClassName('left-side-matrix')
    linkLeftMatricesToRight(leftSideMatrices, rightSideMatrices);
    makeRightMatricesEditable(rightSideMatrices);

    bindActionListeners();
}

function getHTML() {
    function getKernelsHTML() {
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
        return kernelsHTML;
    }

    function getSlidePartHTML(matrices, classAttribute) {
        let result = ''
        for (let i = 0; i < matrices.length; i++) {
            let matrix = matrices[i].matrix;
            let matrixHTML = '<div class="matrixWithAddButton"><table id="' + matrices[i].id + '" class="' + classAttribute + '">';
            for (let j = 0; j < matrix.length; j++) {
                let rowInTable = matrix[j];
                matrixHTML += '<tr>'
                for (let k = 0; k < rowInTable.length; k++) {
                    let value = rowInTable[k];
                    matrixHTML += '<td>' + value + '</td>';
                }
                matrixHTML += '</tr>'
            }
            matrixHTML += '</table>';
            matrixHTML += '<button type="button" class="btn btn-primary btn-sm">+</button></div>';
            result = matrixHTML;
        }
        return result;
    }

    let leftSideMatrices = [];
    let rightSideMatrices = [];
    for (let i = 0; i < state.matrices.length; i++) {
        let matrix = state.matrices[i];
        if (matrix.slideNumber === state.currentSlideNumber) {
            leftSideMatrices.push({
                matrix: matrix.matrixValue,
                id: matrix.matrixId
            });
        }
        if (matrix.slideNumber === state.currentSlideNumber + 1) {
            rightSideMatrices.push({
                matrix: matrix.matrixValue,
                id: matrix.matrixId
            });
        }
    }

    //todo add clear button to footer
    return `
        <div class="lab">
            <div class="lab-table">
                <div class="lab-header_text">Алгоритм последовательного распространения сигнала в свёрточной нейронной сети</div>
                <div class="header-buttons">
                    <span class="kernel-caption">Ядра свёртки:</span>
                    ${getKernelsHTML()}
                    <span class="activation-func-caption">Функция активации:</span>
                    <span class="activation-func-value">${generatedVariant.activationFunction}</span>
                    <span class="subsampling-func-caption">Функция подвыбоки:</span>
                    <span class="subsampling-func-value">${generatedVariant.subSamplingFunction}</span>
                    <button type="button" class="btn btn-info showReference" data-toggle="modal" data-target="#exampleModalScrollable">Справка</button>
                </div>
                <div class="header-end-line"></div>
                <div class="slide">
                    <div id="slide-left-part">
                        ${getSlidePartHTML(leftSideMatrices, "left-side-matrix")}
                    </div>
                    <div class="slide-division-line"></div>
                    <div id="slide-right-part">
                        ${getSlidePartHTML(rightSideMatrices, "right-side-matrix")}
                    </div>
                </div>
                
                <div id="addMatrixButtonModal" class="modal">
                    <div class="modal-content">
                        <div class="modal-header">
                            <span class="close-modal">&times;</span>
                            <h2>Добавить новую таблицу</h2>
                        </div>
                        <div class="modal-body">
                            <input id="width-modal" type="number" value="1" placeholder="Ширина"/>
                            <input id="height-modal" type="number" value="1" placeholder="Высота"/>
                            <input id="modal-confirm-button" type="button" value="Добавить">
                        </div>
                    </div>
                </div>
                
                <div class="footer-begin-line"></div>
                <div class="footer">
                    <div class="next-prev-buttons">
                        <input id="prevButton" class="prevButton btn btn-primary" type="button" value="К предыдущему слою" ${state.currentSlideNumber === 0 ? "disabled" : ""}>
                        <input id="clearButton" class="clearButton btn btn-danger" type="button" value="Очистить текущий слой">
                        <input id="nextButton" class="nextButton btn btn-primary" type="button" value="К следующему слою" ${state.currentSlideNumber === 4 || state.currentSlideIsEmpty ? "disabled" : ""}/>
                    </div>
                    <div class="mse-value">
                        <span>MSE:</span>
                        <input type='number' class='mse-value-input' id="MSE_value" value="0"'/>
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

function bindActionListeners() {
    document.getElementById("MSE_value").addEventListener('change', () => {
        updateState((state) => {
            if (isNaN(document.getElementById("MSE_value").value)) {
                return {
                    ...state,
                    mse: 0,
                }
            }
            return {
                ...state,
                mse: Number(document.getElementById("MSE_value").value),
            }
        });
    });

    document.getElementById("prevButton").addEventListener('click', () => {
        updateState((state) => {
            return {
                ...state,
                currentSlideNumber: state.currentSlideNumber - 1
            }
        });
        rerender();
    });

    document.getElementById("nextButton").addEventListener('click', () => {
        updateState((state) => {
            return {
                ...state,
                currentSlideNumber: state.currentSlideNumber + 1
            }
        });
        rerender();
    });

    //clear button

    var buttons = document.getElementsByClassName("btn-sm");
    for (let i = 0; i < buttons.length; i++) {
        let modal = document.getElementById("addMatrixButtonModal");
        let clearButton = buttons.item(i)
        let span = document.getElementsByClassName("close-modal")[0];

        // When the user clicks the button, open the modal
        clearButton.onclick = function () {
            modal.style.display = "block";
        }

        // When the user clicks on <span> (x), close the modal
        span.onclick = function () {
            modal.style.display = "none";
        }

        // When the user clicks anywhere outside of the modal, close it
        window.onclick = function (event) {
            if (event.target === modal) {
                modal.style.display = "none";
            }
        }

        let confirmButton = document.getElementById("modal-confirm-button");
        confirmButton.onclick = function () {
            let widthElement = document.getElementById("width-modal")
            let heightElement = document.getElementById("height-modal")
            console.log('creating new matrix with ' + widthElement.value + 'x' + heightElement.value)
            //todo implement
        }

    }
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
                            matrices: [
                                //first matrix of first slide
                                {
                                    matrixId: 'id-' + (matrixIdCounter++),
                                    slideNumber: 0,
                                    matrixValue: generatedVariant.inputMatrix,
                                    linkedMatricesIds: []
                                }
                            ],
                            currentSlideNumber: 0,
                            currentSlideIsEmpty: true,
                            mse: 0
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
            let result = {
                matrices: state.matrices,
                mse: state.mse
            }
            console.log('getResultsValue', result);
            return JSON.stringify(result);
        },
        calculateHandler: function (text, code) {
        },
    }
}

var Vlab = init_lab();