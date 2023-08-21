const sortByTimestamp = (data) =>
    data.sort((item1, item2) => new Date(item1.timestamp) - new Date(item2.timestamp));

const compareProducedAndAnnouncedDataByTimestamp = (produced, announced) =>
    produced.every((item, index) =>
        new Date(item.timestamp).getTime() === new Date(announced[index].timestamp).getTime()
    );

const computeValeurGraph = ({produced, announced, tolerance}) => {
    return produced.reduce((valeurGraph, item, index) => {
        const gap = announced[index].value - item.value;
        valeurGraph.push({timestamp: item.timestamp, value: gap > tolerance ? 0 : 1})

        return valeurGraph
    }, []);
}

const compute = (data) => {
    const {produced, announced, tolerance} = data;
    if (produced?.length !== announced?.length) {
        throw new Error('Lists length are not equal')
    }
    const sortedProducedData = sortByTimestamp(produced);
    const sortedAnnouncedData = sortByTimestamp(announced);
    if (!compareProducedAndAnnouncedDataByTimestamp(sortedProducedData, sortedAnnouncedData)) {
        throw new Error("The timestamps do not correspond to the same indexes.")
    }
    //in this case we must compare the values between produced and announced
    return computeValeurGraph({produced, announced, tolerance})
}

// module.exports = {
//     compute,
//     sortByTimestamp,
//     computeValeurGraph,
//     compareProducedAndAnnouncedDataByTimestamp
// }
