import React, {Component, PropTypes} from 'react';
import {
  NativeModules,
  findNodeHandle,
  requireNativeComponent, 
  View
} from 'react-native';
var UIManager = require('UIManager');

const MPLineChart = requireNativeComponent('MPLineChart', LineChart);

class LineChart extends Component {
    constructor(props) {
        super(props);
    }

    shouldComponentUpdate(nextProps, nextState) {
      // NOTE - Never update the line chart. It will render the whole graph again,
      // thus breaking the customHighlightVal logic flow.
      // Currently, there is never a use-case to do so.
      // If there is ever a case, make sure to put the logic for customHighlightVal 
      // in an else and return false.
      console.log("Inside ShouldComponentUpdate");
      console.log("Custom highlightVal value -", nextProps.customHighlightVal)
      console.log("Calling method");

      UIManager.dispatchViewManagerCommand(
        findNodeHandle(this),
        UIManager.MPLineChart.Commands.customHighlightValMethod,
        []
      );
      
      /*
      // Getting the customValue here.
      if(typeof nextProps.customHighlightVal !== "undefined"){
        let hVal = parseInt(nextProps.customHighlightVal);

        if(!isNaN(hVal) && hVal >= 0){
          //MPLineChart.customHighlightVal(findNodeHandle(this), hVal); 
        }
      }
      */

      return false;
    }


    render() {
        return (
            <MPLineChart {...this.props}/>
        );
    }
}

LineChart.propTypes = {
    ...View.propTypes,
    data:PropTypes.object,
    touchEnabled:PropTypes.bool,
    dragEnabled:PropTypes.bool,
    scaleEnabled:PropTypes.bool,
    scaleXEnabled:PropTypes.bool,
    scaleYEnabled:PropTypes.bool,
    pinchZoom:PropTypes.bool,
    doubleTapToZoomEnabled:PropTypes.bool,
    highlightPerDragEnabled:PropTypes.bool,
    highlightPerTapEnabled:PropTypes.bool,
    dragDecelerationEnabled:PropTypes.bool,
    dragDecelerationFrictionCoef:PropTypes.number,
    maxVisibleValueCount:PropTypes.number,
    limitLine:PropTypes.object,
    description:PropTypes.string,
    backgroundColor:PropTypes.string,
    drawGridBackground:PropTypes.bool,
    gridBackgroundColor:PropTypes.string,
    visibleXRange:PropTypes.array,
    borderColor:PropTypes.string,
    borderWidth:PropTypes.number,
    xAxis:PropTypes.object,
    yAxisLeft:PropTypes.object,
    yAxisRight:PropTypes.object,
    yAxis:PropTypes.object,
    fitScreen:PropTypes.bool,
    chartPadding:PropTypes.string,
    legend:PropTypes.object,
    viewCenter: PropTypes.array,
    zoomTo: PropTypes.object,
    extraOffsets: PropTypes.string
}

export default LineChart;
