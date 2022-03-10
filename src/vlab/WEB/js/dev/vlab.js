function getHTML(templateData) {
    //todo extract html from state

    let tableData = "";
    let countInvalidNodesValue = 0;
    if (templateData.nodesValue) {
        for (let i = 0, l = templateData.nodesValue.length; i < l; i++) {
            countInvalidNodesValue += (templateData.nodesValue[i] === null) ? 1 : 0;
        }
    }

    for (let i = 0; i < templateData.neuronsTableData.length; i++) {
        let currentNodeSection = [templateData.neuronsTableData[i].nodeSection].toString().replaceAll("n", "");
        if (currentNodeSection.length === 0)
            currentNodeSection = "-";

        tableData += `<tr>
            <td>
                ${templateData.neuronsTableData[i].nodeId.substring(1)}
            </td>
            <td>
                ${currentNodeSection}
            </td>            
            <td>
                ${templateData.neuronsTableData[i].neuronInputSignalValue}            
            </td>
            <td>
                ${templateData.neuronsTableData[i].neuronOutputSignalValue}            
            </td>
        </tr>`;
    }

    let currentNeuronInputSignalValue = `<input id="currentNeuronInputSignalValue" placeholder="Введите число" class="tableInputData" type="number" value="${templateData.currentNeuronInputSignalValue}"/>`
    let currentNeuronOutputSignalValue = `<input id="currentNeuronOutputSignalValue" placeholder="Введите число" class="tableInputData" type="number" value="${templateData.currentNeuronOutputSignalValue}"/>`

    if (templateData.currentStep !== templateData.inputNeuronsAmount + templateData.amountOfHiddenLayers * templateData.amountOfNodesInHiddenLayer + templateData.outputNeuronsAmount) {
        let currentNodeSection = [...templateData.currentNodeSection];
        for (let i = 0; i < currentNodeSection.length; i++) {
            currentNodeSection[i] = currentNodeSection[i].substring(1);
        }

        if (currentNodeSection.length === 0)
            currentNodeSection = "-";

        tableData += `<tr>
            <td>
                ${templateData.currentSelectedNodeId ? templateData.currentSelectedNodeId.substring(1) : ""}
            </td>
            <td>
                ${currentNodeSection}
            </td>          
            <td>            
                ${currentNeuronInputSignalValue}
            </td>
            <td>
                ${currentNeuronOutputSignalValue}                
            </td>
        </tr>`;
    }

    return `
        <div class="lab">
            <div class="lab-table">                                                                         
                <div class="lab-header_text">Алгоритм последовательного распространения сигнала в нейронной сети.</div>             
                <div class="header-buttons">
                    <button type="button" class="btn btn-info redrawGraph">Перерисовать граф</button>
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
                    <table class="steps-table">
                        <tr>
                            <th>X</th>
                            <th>Прообразы X</th>                                   
                            <th>input(X)</th>
                            <th>output(X)</th>
                        </tr>                        
                        ${tableData}                                        
                    </table>                             
                    <div class="maxFlow">
                        <span>MSE:</span>
                        <input type='number' ${countInvalidNodesValue !== 0 ? "disabled" : ""} class='maxFlow-input' id="error" value="${templateData.error}"'/>                       
                    </div>                                                                                                                                            
                </div>                    
            </div> 
            <div class="lab-header">                                       
                    <!-- Button trigger modal -->
                                       
                    <!-- Modal -->
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
                                <p>Если нейронная сеть на рисунке отображена плохо, то воспользуйтесь кнопкой <b>«Перерисовать»</b> до тех пор, пока рисунок и нанесенные числовые значения будут хорошо видны. </p>

                                <p>У нейронов входного слоя <b>в скобках</b> указаны их входные сигналы. Для заполнения очередной строки таблицы <b>щелкните по выбранной вершине графа</b>. Вершина поменяет цвет на <b>красный</b> и будет занесена в таблицу. Если у нейрона есть прообразы, то <b>щелкните по каждой такой вершине</b> на рисунке: нейроны прообразов будут занесены в таблицу.</p> 
                                
                                <p>Определите входной и выходной сигнал нейрона, внесите в таблицу их значения после <b>округления до второго знака после запятой</b>. Для перехода к следующей строке таблицы нажмите <b>кнопку «+»</b>. Если очередная строка заполнена неверно, то используйте <b>кнопку «-»</b>, а после этого создайте эту строку в таблице с помощью кнопки «+» и заполните ее еще раз. После этого нейрон на рисунке поменяет цвет на зеленый.</p> 
                                
                                <p>Завершить формирование таблицы, когда на рисунке все нейроны будут раскрашены зеленым цветом.</p>
                                 
                                <p>Рассчитайте и введите значение оценки полученного решения MSE после округления до второго знака после запятой. После этого нажмите кнопку в правом нижнем углу стенда <b>«Ответ готов»</b>.</p>
                          </div>                                 
                        </div>
                      </div>
                    </div>                           
                </div>                                                                      
        </div>`;
}

function initState() {
    //todo state proposal: existent linked matrices with answers; current slide number.
    // (rerender slides each time slide is changing)

    const sigmoidFunction = "сигмовидная";
    const linearFunction = "линейная";
    const tgFunction = "гиперболический тангенс";

    let _state = {
        currentNodeSection: [],
        neuronsTableData: [],
        currentSelectedNodeId: "",
        prevSelectedNodeId: "",
        prevNeuronInputSignalValue: "",
        prevNeuronOutputSignalValue: "",
        prevNodeSection: [],
        currentNeuronInputSignalValue: "",
        currentNeuronOutputSignalValue: "",
        currentActivationFunction: "",
        error: 0,
        isSelectingNodesModeActivated: false,
        currentStep: 0,
        inputNeuronsAmount: 0,
        outputNeuronsAmount: 0,
        amountOfHiddenLayers: 0,
        amountOfNodesInHiddenLayer: 0,
        activationFunctions: [sigmoidFunction, linearFunction, tgFunction],
        sigmoidFunction,
        linearFunction,
        tgFunction,
    };

    return {
        getState: function () {
            return _state
        },
        updateState: function (callback) {
            _state = callback(_state);
            return _state;
        }
    }
}

function subscriber() {
    //todo eliminate redundant logic (only rerender is required)
    const events = {};

    return {
        subscribe: function (event, fn) {
            if (!events[event]) {
                events[event] = [fn]
            } else {
                events[event] = [fn];
            }

        },
        emit: function (event, data = undefined) {
            events[event].map(fn => data ? fn(data) : fn());
        }
    }
}

function bindActionListeners(appInstance) {
    //todo eliminate redundant logic

    document.getElementById("error").addEventListener('change', () => {
        const state = appInstance.state.updateState((state) => {

            if (isNaN(document.getElementById("error").value)) {
                return {
                    ...state,
                    error: 0,
                }
            }

            return {
                ...state,
                error: Number(document.getElementById("error").value),
            }
        });

        appInstance.subscriber.emit('render', state);
    });

    if (appInstance.state.getState().currentStep !== appInstance.state.getState().inputNeuronsAmount + appInstance.state.getState().amountOfNodesInHiddenLayer * appInstance.state.getState().amountOfHiddenLayers + appInstance.state.getState().outputNeuronsAmount) {
        document.getElementById("currentNeuronOutputSignalValue").addEventListener('change', () => {
            const state = appInstance.state.updateState((state) => {
                return {
                    ...state,
                    currentNeuronOutputSignalValue: Number(document.getElementById("currentNeuronOutputSignalValue").value),
                }
            });

            appInstance.subscriber.emit('render', state);
        });

        document.getElementById("currentNeuronInputSignalValue").addEventListener('change', () => {
            const state = appInstance.state.updateState((state) => {
                return {
                    ...state,
                    currentNeuronInputSignalValue: Number(document.getElementById("currentNeuronInputSignalValue").value),
                }
            });

            appInstance.subscriber.emit('render', state);
        });
    }

    document.getElementById("addStep").addEventListener('click', () => {
        // перересовываем приложение
        appInstance.subscriber.emit('render', state);
    });

    document.getElementsByClassName('redrawGraph')[0].addEventListener('click', () => {
        // перересовываем приложение
        appInstance.subscriber.emit('render', state);
    });

    document.getElementsByClassName("minusStep")[0].addEventListener('click', () => {
        // перересовываем приложение
        appInstance.subscriber.emit('render', state);
    });
}

function init_lab() {
    const appInstance = {
        state: initState(),
        subscriber: subscriber(),
    };

    return {
        setletiant: function (str) {
        },
        setPreviosSolution: function (str) {
        },
        setMode: function (str) {
        },

        init: function () {
            if (document.getElementById("preGeneratedCode")) {
                if (document.getElementById("preGeneratedCode").value !== "") {
                    appInstance.state.updateState((state) => {
                        console.log(document.getElementById("preGeneratedCode").value, 'beforeParse');
                        let variant = JSON.parse(document.getElementById("preGeneratedCode").value);
                        console.log(variant);

                        //todo get initial state from variant
                        return {

                        }
                    });
                }
            }

            const root = document.getElementById('jsLab');

            // основная функция для рендеринга
            const render = (state) => {
                console.log('state', state);
                //todo extract html from state
                root.innerHTML = getHTML({...state});
                bindActionListeners(appInstance);
            };

            appInstance.subscriber.subscribe('render', render);
            // first render request
            appInstance.subscriber.emit('render', appInstance.state.getState());
            },

        getCondition: function () {
        },
        getResults: function () {
            //todo get results from state
            let result = {qwe: "qwerty"};
            console.log('getResults', result);
            return JSON.stringify(result);
        },
        calculateHandler: function (text, code) {
        },
    }
}

var Vlab = init_lab();