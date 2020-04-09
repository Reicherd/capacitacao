System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    function logarTempoDeExecucao(segundos = false) {
        return function (target, propertyKey, descriptor) {
            const metodo = descriptor.value;
            descriptor.value = function (...args) {
                console.log("_______________________________");
                let t1 = performance.now();
                const retorno = metodo.apply(this, args);
                let t2 = performance.now();
                console.log(`La preformace de ${propertyKey} é ${segundos ? (t2 - t1) / 1000 : (t2 - t1)}`);
                return retorno;
            };
            return descriptor;
        };
    }
    exports_1("logarTempoDeExecucao", logarTempoDeExecucao);
    return {
        setters: [],
        execute: function () {
        }
    };
});
