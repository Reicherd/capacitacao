System.register([], function (exports_1, context_1) {
    "use strict";
    var __moduleName = context_1 && context_1.id;
    function throttle(ms = 500) {
        return function (target, propertyKey, descriptor) {
            const metodo = descriptor.value;
            let timer = 0;
            descriptor.value = function (...args) {
                event ? event.preventDefault() : null;
                clearInterval(timer);
                timer = setTimeout(() => {
                    metodo.apply(this, args);
                }, ms);
            };
            return descriptor;
        };
    }
    exports_1("throttle", throttle);
    return {
        setters: [],
        execute: function () {
        }
    };
});
