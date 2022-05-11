let matrixIdCounter = 0;

//global mutable state
let state = {
    matrices: [],
    currentSlideNumber: 0,
    currentSlideIsEmpty: true,
    mse: 0,
};

let lines = [];

//info about generated variant, will be filled only once on initialization
let generatedVariant = {
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

function linkLeftMatricesToRight(leftSideMatrices) {
    for (let i = 0; i < leftSideMatrices.length; i++) {
        let matrixElement = leftSideMatrices.item(i);
        let leftMatrixId = matrixElement.id
        for (let j = 0; j < state.matrices.length; j++) {
            if (state.matrices[j].matrixId === leftMatrixId) {
                let linkedMatrices = state.matrices[j].linkedMatricesIds;
                for (let k = 0; k < linkedMatrices.length; k++) {
                    let linkedMatrixId = linkedMatrices[k];
                    let linkedMatrixElement = document.getElementById(linkedMatrixId)
                    let line = new LeaderLine(
                        matrixElement,
                        linkedMatrixElement
                    );
                    line.size = 3;
                    line.color = 'rgba(30, 130, 250, 0.5)';
                    line.dash = true
                    line.animation = true
                    line.opacity = 0.7
                    lines.push(line)
                }
            }
        }
    }
}

function makeMatricesEditable(matrices) {
    for (let i = 0; i < matrices.length; i++) {
        let tableElement = matrices.item(i);
        let matrixId = tableElement.id;
        let tds = tableElement.getElementsByTagName('td');
        let cellsInOneRowCnt = tds.length / tableElement.getElementsByTagName('tr').length;

        for (let j = 0; j < tds.length; j++) {
            tds[j].setAttribute('row_val', Math.floor(j / cellsInOneRowCnt))
            tds[j].setAttribute('column_val', j % cellsInOneRowCnt)
            tds[j].onclick = function () {
                if (this.hasAttribute('data-clicked')) {
                    return;
                }

                this.setAttribute('data-clicked', 'yes')
                this.setAttribute('data-text', this.innerHTML)

                let input = document.createElement('input');
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
                    let td = input.parentElement
                    let orig_text = td.getAttribute('data-text')
                    let current_text = this.value

                    if (orig_text !== current_text) {
                        let rowVal = td.getAttribute('row_val')
                        let columnVal = td.getAttribute('column_val')

                        td.removeAttribute('data-clicked')
                        td.removeAttribute('data-text')
                        td.innerHTML = current_text
                        td.style.cssText = 'padding: 5px'

                        for (let k = 0; k < state.matrices.length; k++) {
                            if (state.matrices[k].matrixId === matrixId) {
                                state.matrices[k].matrixValue[rowVal][columnVal] = parseFloat(current_text);
                            }
                        }
                    } else {
                        td.removeAttribute('data-clicked')
                        td.removeAttribute('data-text')
                        td.innerHTML = orig_text
                        td.style.cssText = 'padding: 5px'
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

    for (let i = 0; i < lines.length; i++) {
        lines[i].remove()
    }
    lines.length = 0;

    document.getElementById('jsLab').innerHTML = getHTML();

    linkLeftMatricesToRight(document.getElementsByClassName('left-side-matrix'));
    makeMatricesEditable(document.getElementsByClassName('right-side-matrix'));

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
            let buttonHTML = '<button type="button" class="btn btn-primary btn-sm">+</button>';
            if (classAttribute === 'right-side-matrix') {
                buttonHTML = '';
            }
            matrixHTML += buttonHTML + '</div>';
            result += matrixHTML;
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

    return `
        <div class="lab">
            <div class="lab-table">
                <div class="lab-header_text">Алгоритм последовательного распространения сигнала в свёрточной нейронной сети</div>
                <div class="header-buttons">
                    <span class="kernel-caption">Ядра свёртки:</span>
                    ${getKernelsHTML()}
                    <span class="activation-func-caption">Функция активации:</span>
                    <span class="activation-func-value">${generatedVariant.activationFunction}</span>
                    <span class="subsampling-func-caption">Функция подвыборки:</span>
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
                
                <div id="addMatrixButtonModal">
                    <div class="addMatirx-modal-content">
                        <div class="addMatirx-modal-header">
                            <span class="close-addMatrix-modal">&times;</span>
                            <h5>Добавить новую таблицу</h5>
                        </div>
                        <div class="addMatirx-modal-body">
                            <input id="width-modal" type="number" required placeholder="Ширина"/>
                            <input id="height-modal" type="number" required placeholder="Высота"/>
                            <input id="modal-confirm-button" type="button" class="btn-info" value="Добавить">
                        </div>
                    </div>
                </div>
                
                <div class="footer-begin-line"></div>
                <div class="footer">
                    <div class="next-prev-buttons">
                        <input id="prevButton" class="prevButton btn btn-primary" type="button" value="К предыдущему слою" ${state.currentSlideNumber === 0 ? "disabled" : ""}>
                        <input id="clearButton" class="clearButton btn btn-danger" type="button" value="Очистить слой">
                        <input id="nextButton" class="nextButton btn btn-primary" type="button" value="К следующему слою" ${state.currentSlideNumber === 3 || state.currentSlideIsEmpty ? "disabled" : ""}/>
                    </div>
                    <div class="mse-value">
                        <span>MSE:</span>
                        <input type='number' class='mse-value-input' id="MSE_value" value="${state.mse}"'/>
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
                                <p>Используя указанные ядра свёртки, функцию активации, функцию подвыборки, рассчитайте значения нейронов на каждом слое сети.</p>
                                <p>Для создания матрицы на следующем слое сети нажмите на <b>кнопку «+»</b> у оригинальной матрицы, после чего введите ширину и высоту новой матрицы.</p>
                                <p>Для редактирования какой-либо ячейки созданной матрицы, нажмите на соответствующую ячейку и введите значение, в качестве разделителя используйте <b>точку</b>.</p>
                                <p>Если вы хотите удалить созданную матрицу, используйте кнопку <b>«Очистить слой»</b>. 
                                Будьте осторожны, данное действие удалит все матрицы на следующих слоях сети!</p>
                                <p>Определяя значения сигнала нейрона, используйте <b>округление до второго знака после запятой</b>.</p>
                                <p>Рассчитайте и введите значение оценки полученного решения MSE после округления до второго знака после запятой. 
                                После этого нажмите кнопку в правом нижнем углу стенда <b>«Ответ готов»</b>.</p> 
                          </div>
                        </div>
                      </div>
                    </div>
                </div>
        </div>`;
}

function emptyMatrixWithSize(height, width) {
    let result = [];
    for (let i = 0; i < height; i++) {
        let row = []
        for (let j = 0; j < width; j++) {
            row.push(0);
        }
        result.push(row)
    }
    return result;
}

function bindActionListeners() {
    //changing of MSE
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

    //prev button clicked
    document.getElementById("prevButton").addEventListener('click', () => {
        updateState((state) => {
            return {
                ...state,
                currentSlideIsEmpty: false,
                currentSlideNumber: state.currentSlideNumber - 1
            }
        });
        rerender();
    });

    //next button clicked
    document.getElementById("nextButton").addEventListener('click', () => {
        let rightPartMatricesExist = false;
        for (let i = 0; i < state.matrices.length; i++) {
            if (state.matrices[i].slideNumber === state.currentSlideNumber + 2) {
                rightPartMatricesExist = true;
            }
        }

        updateState((state) => {
            return {
                ...state,
                currentSlideIsEmpty: !rightPartMatricesExist,
                currentSlideNumber: state.currentSlideNumber + 1
            }
        });

        rerender();
    });

    function removeItem(array, item) {
        for (let i in array) {
            if (array[i] === item) {
                array.splice(i, 1);
                break;
            }
        }
    }

    document.getElementById("clearButton").addEventListener('click', () => {
        state.currentSlideIsEmpty = true;

        //remove all matrices from current slide right part and following slides
        let matricesToRemove = []
        for (let i = 0; i < state.matrices.length; i++) {
            if (state.matrices[i].slideNumber > state.currentSlideNumber) {
                matricesToRemove.push(state.matrices[i].matrixId);
            }
        }

        matrixIdCounter -= matricesToRemove.length;
        for (let i = 0; i < matricesToRemove.length; i++) {
            let id = matricesToRemove[i]
            for (let j = 0; j < state.matrices.length; j++) {
                let matrix = state.matrices[j]
                removeItem(matrix.linkedMatricesIds, id);
                if (matrix.matrixId === id) {
                    removeItem(state.matrices, matrix);
                }
            }
        }

        rerender();
    })


    //add matrix buttons logic (modal windows)
    let modal = document.getElementById("addMatrixButtonModal");

    window.onclick = function (event) {
        if (event.target === modal) {
            modal.style.display = "none";
            modal.removeAttribute('original_matrix_id')
        }
    }

    let span = document.getElementsByClassName("close-addMatrix-modal")[0];
    span.onclick = function () {
        modal.style.display = "none";
        modal.removeAttribute('original_matrix_id')
    }

    let buttons = document.getElementsByClassName("btn-sm");
    for (let i = 0; i < buttons.length; i++) {
        let addMatrixButton = buttons.item(i)
        addMatrixButton.onclick = function () {
            modal.style.display = "block";
            modal.setAttribute('original_matrix_id', this.parentElement.getElementsByTagName('table')[0].id)
        }
    }

    let confirmButton = document.getElementById("modal-confirm-button");
    confirmButton.onclick = function () {
        let widthElement = document.getElementById("width-modal")
        let heightElement = document.getElementById("height-modal")

        function extractMatrixId(matrixId) {
            return parseInt(matrixId.toString().substring(3)); // skip "id-" and convert to integer
        }

        function crossLinkingMatrixCreationAttempt(originalMatrixId) {
            let origMatrixId = extractMatrixId(originalMatrixId)

            for (let i = 0; i < state.matrices.length; i++) {
                let curMatrix = state.matrices[i]
                if (curMatrix.slideNumber === state.currentSlideNumber &&
                        extractMatrixId(curMatrix.matrixId) > origMatrixId) {
                    if (curMatrix.linkedMatricesIds.length > 0) {
                        return true
                    }
                }
            }
            return false;
        }

        if (widthElement.value && heightElement.value) {
            let width = parseFloat(widthElement.value)
            let height = parseFloat(heightElement.value)
            if (Number.isInteger(width) && Number.isInteger(height)) {
                if (width <= 0 || width > 6 ||
                    height <= 0 || height > 6) {
                    alert('Ширина и высота должны быть положительными числами не превосходящими размеров исходной матрицы');
                } else {
                    console.log('trying to create new matrix with ' + width + 'x' + height)
                    let originalMatrixId = modal.getAttribute('original_matrix_id')

                    //prohibit cross-linking matrices creation
                    if (crossLinkingMatrixCreationAttempt(originalMatrixId)) {
                        alert("Создавать матрицы с перекрёстными связями запрещено!");
                        return;
                    }

                    let oldState = JSON.parse(JSON.stringify(state));
                    let newId = "id-" + (matrixIdCounter++);
                    let newMatrix = {
                        matrixId: newId,
                        slideNumber: state.currentSlideNumber + 1,
                        matrixValue: emptyMatrixWithSize(height, width),
                        linkedMatricesIds: []
                    }
                    state.matrices.push(newMatrix);

                    //add link to new matrix from original one
                    for (let j = 0; j < state.matrices.length; j++) {
                        if (state.matrices[j].matrixId === originalMatrixId) {
                            state.matrices[j].linkedMatricesIds.push(newId);
                            break;
                        }
                    }
                    state.currentSlideIsEmpty = false;

                    rerender()

                    if (checkOverflow(document.getElementById("slide-right-part"))) {
                        //show message and revert to old state
                        alert('Невозможно создать матрицу с заданным размером на текущем слое');
                        state = oldState
                        matrixIdCounter--
                        rerender()
                    }
                }
            } else {
                alert('Ширина и высота должны быть целыми числами');
            }
        } else {
            alert('Введите, пожалуйста, ширину и высоту новой матрицы');
        }
    }

    document.getElementById("slide-right-part").addEventListener("scroll", () => {
        //redraw lines between matrices
        for (let i = 0; i < lines.length; i++) {
            lines[i].position()
        }
    })
}

function checkOverflow(element) {
    const curOverflow = element.style.overflow;
    if ( !curOverflow || curOverflow === "visible" ) {
        element.style.overflow = "hidden";
    }
    element.style.overflow = curOverflow;
    return element.clientWidth < element.scrollWidth
        || element.clientHeight < element.scrollHeight;
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
                                //input matrix
                                {
                                    matrixId: "id-" + (matrixIdCounter++), //matrixIdCounter is equal to 1
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