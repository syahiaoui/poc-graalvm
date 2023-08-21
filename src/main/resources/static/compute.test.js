const {describe, test} = require('node:test');
const assert = require('node:assert/strict');
const {
    compute,
    sortByTimestamp,
    computeValeurGraph,
    compareProducedAndAnnouncedDataByTimestamp
} = require('./compute')

describe('compute', () => {
    test('should throw an error when producedList and announcedList not have same length', () => {
        const data = {
            produced: [],
            announced: [{timestamp: "2023-01-01T00:00:00", value: 12.2}],
        };
        assert.throws(() => compute(data), {message: 'Lists length are not equal'});
    })
    test('should throw an error when producedList is undefined', () => {
        const data = {
            announced: [{timestamp: "2023-01-01T00:00:00", value: 12.2}],
        };
        assert.throws(() => compute(data), {message: 'Lists length are not equal'});
    })
    test('should throw an error when timestamp values are not equal against same index', () => {
        const data = {
            produced: [{timestamp: "2023-01-01T00:00:00", value: 13}],
            announced: [{timestamp: "2023-01-01T00:01:00", value: 12.2}],
            tolerance: 1
        }
        assert.throws(() => compute(data), {message: 'The timestamps do not correspond to the same indexes.'});
    })
    test('should compute the valeur graph', () => {
        const data = {
            produced: [{timestamp: "2023-01-01T00:00:00", value: 13}],
            announced: [{timestamp: "2023-01-01T00:00:00", value: 12.2}],
            tolerance: 1
        }
        assert.deepStrictEqual(compute(data), [
            {
                timestamp: '2023-01-01T00:00:00',
                value: 1
            }
        ])
    })
})

describe('sortByTimestamp', () => {
    test('should sort the array ascending', () => {
        const data = [
            {timestamp: "2023-01-01T00:00:00", value: 13},
            {timestamp: "2023-01-01T05:00:00", value: 12.2},
            {timestamp: "2023-01-01T01:00:00", value: 12.2}
        ];

        const expectedData = [
            {
                timestamp: '2023-01-01T00:00:00',
                value: 13
            },
            {
                timestamp: '2023-01-01T01:00:00',
                value: 12.2
            },
            {
                timestamp: '2023-01-01T05:00:00',
                value: 12.2
            }
        ]
        assert.deepEqual(sortByTimestamp(data), expectedData);
    });
});

describe('compareProducedAndAnnouncedDataByTimestamp', () => {
    test('should return false if timestamps in both arrays do not match', () => {
        const data = {
            produced: [{timestamp: "2023-01-01T00:00:00", value: 13}],
            announced: [{timestamp: "2023-01-01T00:01:00", value: 12.2}],
            tolerance: 1
        }
        assert.strictEqual(compareProducedAndAnnouncedDataByTimestamp(data.produced, data.announced), false)
    });
    test('should return true if timestamps in both arrays are equals', () => {
        const data = {
            produced: [{timestamp: "2023-01-01T00:01:00", value: 13}],
            announced: [{timestamp: "2023-01-01T00:01:00", value: 12.2}],
            tolerance: 1
        }
        assert.strictEqual(compareProducedAndAnnouncedDataByTimestamp(data.produced, data.announced), true)
    });
})

describe('computeValeurGraph', () => {
    test('should compute the valeur graph with value 1 if produced value is greater than announced value', () => {
        const data = {
            produced: [{timestamp: "2023-01-01T00:01:00", value: 13}],
            announced: [{timestamp: "2023-01-01T00:01:00", value: 12.2}],
            tolerance: 1
        }
        assert.deepStrictEqual(computeValeurGraph(data), [
                {
                    timestamp: '2023-01-01T00:01:00',
                    value: 1
                }
            ]
        )
    })
    test('should compute the valeur graph with value 1 if produced value is equal than announced value', () => {
        const data = {
            produced: [{timestamp: "2023-01-01T00:01:00", value: 13}],
            announced: [{timestamp: "2023-01-01T00:01:00", value: 13}],
            tolerance: 1
        }
        assert.deepStrictEqual(computeValeurGraph(data), [
                {
                    timestamp: '2023-01-01T00:01:00',
                    value: 1
                }
            ]
        )
    })
    test('should compute the valeur graph with value 0 if produced value is lower than announced value', () => {
        const data = {
            produced: [{timestamp: "2023-01-01T00:01:00", value: 12}],
            announced: [{timestamp: "2023-01-01T00:01:00", value: 14}],
            tolerance: 1
        }
        assert.deepStrictEqual(computeValeurGraph(data), [
                {
                    timestamp: '2023-01-01T00:01:00',
                    value: 0
                }
            ]
        )
    })
})
