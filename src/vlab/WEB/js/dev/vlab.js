function getHTML(templateData) {
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

function renderTemplate(element, html) {
    element.innerHTML = html;
}

function initState() {
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
        // обновляем стейт приложение
        const state = appInstance.state.updateState((state) => {
            let currentStep = state.currentStep;
            let neuronsTableData = state.neuronsTableData.slice();
            let nodesValue = state.nodesValue.slice();
            let currentSelectedNodeIdNumber = state.currentSelectedNodeId.match(/(\d+)/)[0];
            let currentNeuronInputSignalValue = state.currentNeuronInputSignalValue;
            let currentNeuronOutputSignalValue = state.currentNeuronOutputSignalValue;

            let prevSelectedNodeId = state.currentSelectedNodeId;
            let prevNeuronInputSignalValue = state.currentNeuronInputSignalValue;
            let prevNeuronOutputSignalValue = state.currentNeuronOutputSignalValue;
            let prevNodeSection = state.currentNodeSection.slice();

            if (currentNeuronInputSignalValue === "")
                currentNeuronInputSignalValue = 0;
            if (currentNeuronOutputSignalValue === "")
                currentNeuronOutputSignalValue = 0;

            if (neuronsTableData.length < state.inputNeuronsAmount && !isNaN(currentNeuronInputSignalValue) && !isNaN(currentNeuronOutputSignalValue)) {
                nodesValue[currentSelectedNodeIdNumber] = currentNeuronOutputSignalValue;
                currentStep++;
                neuronsTableData.push({
                    nodeId: state.currentSelectedNodeId,
                    neuronInputSignalValue: currentNeuronInputSignalValue,
                    neuronOutputSignalValue: currentNeuronOutputSignalValue,
                    nodeSection: [],
                });
            } else if (state.currentSelectedNodeId.length > 0 && !isNaN(currentNeuronInputSignalValue) && !isNaN(currentNeuronOutputSignalValue)
                && state.currentNodeSection.length > 0) {
                nodesValue[currentSelectedNodeIdNumber] = currentNeuronOutputSignalValue;
                currentStep++;
                neuronsTableData.push({
                    nodeId: state.currentSelectedNodeId,
                    neuronInputSignalValue: currentNeuronInputSignalValue,
                    neuronOutputSignalValue: currentNeuronOutputSignalValue,
                    nodeSection: state.currentNodeSection,
                });
            } else {
                return {
                    ...state,
                }
            }

            return {
                ...state,
                currentStep,
                neuronsTableData,
                nodesValue,
                prevSelectedNodeId,
                prevNeuronInputSignalValue,
                prevNeuronOutputSignalValue,
                prevNodeSection,
                currentSelectedNodeId: "",
                currentNeuronInputSignalValue: "",
                currentNeuronOutputSignalValue: "",
                currentNodeSection: [],
                isSelectingNodesModeActivated: false,
            }
        });

        // перересовываем приложение
        appInstance.subscriber.emit('render', state);
    });

    document.getElementsByClassName('redrawGraph')[0].addEventListener('click', () => {
        const state = appInstance.state.updateState((state) => {
            let yLevelRandomDisplacement = state.nodes.map(node => {
                return 2 + Math.random() * 3; //смещение ноты по Y из-за того, что не видно значение ребра при отрисовке
            });

            return {
                ...state,
                yLevelRandomDisplacement,
            }
        });

        // перересовываем приложение
        appInstance.subscriber.emit('render', state);
    });

    document.getElementsByClassName("minusStep")[0].addEventListener('click', () => {
        // обновляем стейт приложение
        const state = appInstance.state.updateState((state) => {
            if (state.currentStep > 0) {
                let neuronsTableData = state.neuronsTableData.slice();
                let currentSelectedNodeIdNumber = Number(neuronsTableData[neuronsTableData.length - 1].nodeId.match(/(\d+)/)[0]);
                let currentNodeSection = neuronsTableData[neuronsTableData.length - 1].nodeSection;
                let currentNeuronInputSignalValue = neuronsTableData[neuronsTableData.length - 1].neuronInputSignalValue;
                let currentNeuronOutputSignalValue = neuronsTableData[neuronsTableData.length - 1].neuronOutputSignalValue;
                let currentSelectedNodeId = neuronsTableData[neuronsTableData.length - 1].nodeId;

                neuronsTableData.pop();
                let nodesValueCopy = state.nodesValue.slice();
                nodesValueCopy[currentSelectedNodeIdNumber] = null;
                let prevNeuronInputSignalValue = state.currentNeuronInputSignalValue;
                let prevNeuronOutputSignalValue = state.currentNeuronOutputSignalValue;
                return {
                    ...state,
                    neuronsTableData,
                    prevNeuronInputSignalValue,
                    prevNeuronOutputSignalValue,
                    currentStep: state.currentStep - 1,
                    currentSelectedNodeId,
                    currentNeuronInputSignalValue,
                    currentNeuronOutputSignalValue,
                    currentNodeSection,
                    isSelectingNodesModeActivated: false,
                    nodesValue: nodesValueCopy,
                }
            }

            return {
                ...state,
            }
        });

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
            if (document.getElementById("preGeneratedCode").value !== "") {
                appInstance.state.updateState((state) => {
                    console.log(document.getElementById("preGeneratedCode").value, 'beforeParse');
                    let graph = JSON.parse(document.getElementById("preGeneratedCode").value);
                    console.log(graph);
                    let nodes = graph.nodes;
                    let yLevelRandomDisplacement = nodes.map(node => {
                        return 2 + Math.random() * 3; //смещение ноты по Y из-за того, что не видно значение ребра при отрисовке
                    });

                    let initNodesValue = [...graph.nodesValue];
                    initNodesValue.fill(null);
                    graph.nodesValue = [...initNodesValue];

                    return {
                        ...state,
                        ...graph,
                        yLevelRandomDisplacement,
                    }
                });
            }

            const root = document.getElementById('jsLab');

            // основная функция для рендеринга
            const render = (state) => {
                console.log('state', state);
                renderTemplate(root, getHTML({...state}));
                bindActionListeners(appInstance);
            };

            appInstance.subscriber.subscribe('render', render);

            // инициализируем первую отрисовку
            appInstance.subscriber.emit('render', appInstance.state.getState());
        },

        getCondition: function () {
        },
        getResults: function () {
            let result = {...appInstance.state.getState()};
            delete result.edgeWeight;
            console.log('getResults', result);
            return JSON.stringify(result);
        },
        calculateHandler: function (text, code) {
        },
    }
}

var Vlab = init_lab();