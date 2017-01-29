import React, {Component, PropTypes} from 'react';
import {
  NativeModules,
  findNodeHandle,
  requireNativeComponent, 
  View,
  UIManager,
} from 'react-native';

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
      
      // Getting the customValue here.
      if(typeof nextProps.customHighlightVal !== "undefined"){
        let hVal = parseInt(nextProps.customHighlightVal);

        if(!isNaN(hVal) && hVal >= 0){
          // This is significantly different than in iOS. 
          // That's because ViewManagers are involved and not modules, so @ReactMethod won't work here.
          
          // This trick is based off of this link - 
          // https://medium.com/@john1jan/communicating-to-and-from-native-ui-components-in-react-native-android-b8abcfb2f9c8#.vcasx2va3
          // react-native docs for this is notoriously hard to find.
          UIManager.dispatchViewManagerCommand(
            findNodeHandle(this),
            UIManager.MPLineChart.Commands.customHighlightValMethod,
            [hVal]
          );
        }
      }

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
