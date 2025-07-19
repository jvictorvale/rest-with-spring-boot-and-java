package br.com.jvictorvale.controllers;

import br.com.jvictorvale.exception.UnsupportedMathOperationException;
import br.com.jvictorvale.math.SimpleMath;
import br.com.jvictorvale.request.converters.NumberConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/math")
public class MathController {

    private SimpleMath math = new SimpleMath();

    @RequestMapping("/sum/{numberOne}/{numberTwo}")
    public Double sum(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) throws IllegalAccessException {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.sum(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping("/subtraction/{numberOne}/{numberTwo}")
    public Double subtraction(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) throws IllegalAccessException {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.subtraction(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping("/multiplication/{numberOne}/{numberTwo}")
    public Double multiplication(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) throws IllegalAccessException {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.multiplication(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping("/division/{numberOne}/{numberTwo}")
    public Double division(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) throws IllegalAccessException {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.division(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping("/average/{numberOne}/{numberTwo}")
    public Double average(
            @PathVariable("numberOne") String numberOne,
            @PathVariable("numberTwo") String numberTwo
    ) throws IllegalAccessException {
        if(!NumberConverter.isNumeric(numberOne) || !NumberConverter.isNumeric(numberTwo))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.average(NumberConverter.convertToDouble(numberOne), NumberConverter.convertToDouble(numberTwo));
    }

    @RequestMapping("/square-root/{numberOne}")
    public Double squareRoot(
            @PathVariable("numberOne") String numberOne
    ) throws IllegalAccessException {
        if(!NumberConverter.isNumeric(numberOne))
            throw new UnsupportedMathOperationException("Please set a numeric value!");
        return math.squareRoot(NumberConverter.convertToDouble(numberOne));
    }
}
