'use strict'
/**
 * APIKakaoMap,
 * 카카오 맵을 이니셜라이징 시켜주는 함수
 *
 * @param {Object} options 로그인 옵션
 * @param {Object} options.map 맵 오브젝트 (없어도 가능)
 * @param {Object} options.map_options 맵을 생성할 때 초기 옵션 (없어도 가능)
 * @param {Object} options.control 맵 컨트롤 UI
 * @param {Object} options.control.zoomControl 맵 컨트롤 UI (줌 컨트롤)
 * @param {string} options.control.zoomControl.direction 맵 컨트롤 UI (줌 컨트롤 위치)
 * @param {Object} options.control.mapTypeControl 맵 컨트롤 UI (맵 타입 컨트롤)
 * @param {string} options.control.mapTypeControl.direction 맵 컨트롤 UI (맵 타입 위치)
 * @param {string || HTMLElement} options.container 카카오 맵 컨테이너
 * @param {string || url} options.get_key_url 카카오 키 url
 * @param {object} options.marker 마커 (없어도 가능)
 * @param {function} options.initialize 초기화 세팅 완료 시점 콜백 이벤트
 * */
const APIKakaoMap = function (options = {
    map_options, container, control, get_key_url, initialize, markerClicked,
}) {
    this.options = {
        markers: undefined,
        map: undefined,
        map_options: undefined,
        control: undefined,
        container: undefined,
        marker: undefined,
        get_key_url: undefined,
        initialize: undefined,
        mapClicked: undefined,
        mapDragStart: undefined,
        mapDragEnd: undefined,
        mapZoomChanged: undefined,
        markerClicked: undefined,
    };

    this.options.map = options.hasOwnProperty('map') ? options.map : undefined;
    this.options.map_options = options.hasOwnProperty('map_options') ? options.map_options : {
        center: {
            lat: 33.450701,
            lng: 126.570667
        },
        level: 3,
    };
    this.options.control = options.hasOwnProperty('control') ? options.control : {
        zoomControl: {
            direction: 'RIGHT',
        },
        mapTypeControl: {
            direction: 'TOPRIGHT',
        },
    };

    if (!options.container) {
        this.options.container = document.querySelector(options.container);
    } else {
        if (options.container.nodeType !== 1) {
            this.options.container = document.querySelector(options.container);
        } else {
            this.options.container = options.container;
        }
    }
    this.options.markers = options.hasOwnProperty('markers') ? options.markers : [];
    this.options.get_key_url = options.hasOwnProperty('get_key_url') ? options.get_key_url : `/web/resources/assets/datas/getKey.json`;
    this.options.initialize = options.hasOwnProperty('initialize') ? options.initialize : function (options) {
        console.log('initialize', options);
    };
    this.options.mapClicked = options.hasOwnProperty('mapClicked') ? options.mapClicked : function (mouseEvent, latlng) {
        console.log('mouseEvent', mouseEvent, 'latlng', latlng);
    };
    this.options.markerClicked = options.hasOwnProperty('markerClicked') ? options.markerClicked : function (id, data) {
        console.log('id', id, 'data', data);
    };
    this.options.mapDragStart = options.hasOwnProperty('mapDragStart') ? options.mapDragStart : function (latlng) {
        console.log('latlng', latlng);
    };
    this.options.mapDragEnd = options.hasOwnProperty('mapDragEnd') ? options.mapDragEnd : function (latlng) {
        console.log('latlng', latlng);
    };
    this.options.mapZoomChanged = options.hasOwnProperty('mapZoomChanged') ? options.mapZoomChanged : function (level) {
        // 지도의 현재 레벨을 얻어옵니다
        console.log('level', level);
    };

    let _this = this;
    let _options = this.options;

    function build() {
        if (!_options.map) {
            getKey('kakao').then((result) => {
                if (result.status === 'OK') {
                    _options.key = result.data.key;
                    $.getScript(`https://dapi.kakao.com/v2/maps/sdk.js?appkey=${_options.key}&libraries=services,clusterer,drawing&autoload=false`, function () {
                        // Stuff to do after someScript has loaded
                        window.kakao.maps.load(function () {
                            let map = new window.kakao.maps.Map(_options.container, {
                                level: _options.map_options.level,
                                center: new kakao.maps.LatLng(_options.map_options.center.lat, _options.map_options.center.lng)
                            }); // 지도를 생성합니다
                            if (_options.mapClicked) {
                                kakao.maps.event.addListener(map, 'click', function (mouseEvent) {
                                    // 클릭한 위도, 경도 정보를 가져옵니다
                                    let latlng = mouseEvent.latLng;
                                    _options.mapClicked(mouseEvent, latlng);
                                });
                            }
                            if (_options.mapDragStart) {
                                kakao.maps.event.addListener(map, 'dragstart', function () {
                                    // 지도 중심좌표를 얻어옵니다
                                    var latlng = map.getCenter();
                                    _options.mapDragStart(latlng);
                                });
                            }
                            if (_options.mapDragEnd) {
                                kakao.maps.event.addListener(map, 'dragend', function () {
                                    // 지도 중심좌표를 얻어옵니다
                                    var latlng = map.getCenter();
                                    _options.mapDragStart(latlng);
                                });
                            }
                            if (_options.mapZoomChanged) {
                                kakao.maps.event.addListener(map, 'zoom_changed', function () {
                                    // 지도의 현재 레벨을 얻어옵니다
                                    let level = map.getLevel();
                                    _options.mapZoomChanged(level);
                                });
                            }

                            _options.map = map;

                            if (_options.control) {
                                if (_options.control.zoomControl) {
                                    // 지도 확대 축소를 제어할 수 있는  줌 컨트롤을 생성합니다
                                    let zoomControl = new kakao.maps.ZoomControl();
                                    if (_options.control.zoomControl.direction.toLowerCase() === 'top') {
                                        _options.map.addControl(zoomControl, kakao.maps.ControlPosition.TOP);
                                    } else if (_options.control.zoomControl.direction.toLowerCase() === 'bottom') {
                                        _options.map.addControl(zoomControl, kakao.maps.ControlPosition.BOTTOM);
                                    } else if (_options.control.zoomControl.direction.toLowerCase() === 'left') {
                                        _options.map.addControl(zoomControl, kakao.maps.ControlPosition.LEFT);
                                    } else if (_options.control.zoomControl.direction.toLowerCase() === 'right') {
                                        _options.map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
                                    } else if (_options.control.zoomControl.direction.toLowerCase() === 'topright') {
                                        _options.map.addControl(zoomControl, kakao.maps.ControlPosition.TOPRIGHT);
                                    }
                                }

                                if (_options.control.mapTypeControl) {
                                    // 일반 지도와 스카이뷰로 지도 타입을 전환할 수 있는 지도타입 컨트롤을 생성합니다
                                    let mapTypeControl = new kakao.maps.MapTypeControl();
                                    // 지도에 컨트롤을 추가해야 지도위에 표시됩니다
                                    // kakao.maps.ControlPosition은 컨트롤이 표시될 위치를 정의하는데 TOPRIGHT는 오른쪽 위를 의미합니다
                                    if (_options.control.mapTypeControl.direction.toLowerCase() === 'top') {
                                        _options.map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOP);
                                    } else if (_options.control.mapTypeControl.direction.toLowerCase() === 'bottom') {
                                        _options.map.addControl(mapTypeControl, kakao.maps.ControlPosition.BOTTOM);
                                    } else if (_options.control.mapTypeControl.direction.toLowerCase() === 'left') {
                                        _options.map.addControl(mapTypeControl, kakao.maps.ControlPosition.LEFT);
                                    } else if (_options.control.mapTypeControl.direction.toLowerCase() === 'right') {
                                        _options.map.addControl(mapTypeControl, kakao.maps.ControlPosition.RIGHT);
                                    } else if (_options.control.mapTypeControl.direction.toLowerCase() === 'topright') {
                                        _options.map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);
                                    }
                                }
                            }
                            _options.initialize(_this, _options);
                        });
                    });
                } else {
                    throw new Error(`API key is not defined`);
                }
            });
        }
    }

    /**
     * addMarker,
     * 카카오 맵에 마커를 추가하는 함수
     *
     * @param {Object} marker 마커 옵션
     * @param {Object} marker.center 마커 포지션 설정 (lat,lng)
     * @param {float} marker.center.lat 마커 lat 포지션
     * @param {float} marker.center.lng 마커 lng 포지션
     * @param {string} marker.src 마커 커스텀 이미지 주소
     * @param {Object} marker.size 마커 사이즈(width,height)
     * @param {BigInt} marker.size.width 마커 width 사이즈
     * @param {BigInt} marker.size.height 마커 height 사이즈
     * @param {Object} marker.option 마커 옵션(offset)
     * @param {Object} marker.option.offset 마커 위치 (top,left)
     * @param {BigInt} marker.option.offset.top 마커 Top 위치
     * @param {BigInt} marker.option.offset.left 마커 Left 위치
     * */
    this.addMarker = (marker_options = {
        id: tokenGenerator(8),
        data: undefined,
        center: {
            lat: 33.450701,
            lng: 126.570667
        },
        src: 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png',
        size: {
            width: 64,
            height: 69
        },
        option: {
            offset: {
                top: 27,
                left: 69
            }
        }
    }) => {
        let markerImage = new kakao.maps.MarkerImage(marker_options.src,
                new kakao.maps.Size(marker_options.size.width, marker_options.size.height),
                {offset: new kakao.maps.Point(marker_options.option.offset.top, marker_options.option.offset.left)}),
            markerPosition = new kakao.maps.LatLng(marker_options.center.lat, marker_options.center.lng); // 마커가 표시될 위치입니다

        // 마커를 생성합니다
        let marker = new kakao.maps.Marker({
            position: markerPosition,
            image: markerImage // 마커이미지 설정
        });

        //마커 아이디 지정
        marker.id = marker_options.id;
        if (marker_options.data) {
            marker.data = marker_options.data;
        }
        // 마커가 지도 위에 표시되도록 설정합니다
        marker.setMap(_options.map);
        // 마커에 클릭이벤트를 등록합니다
        kakao.maps.event.addListener(marker, 'click', function () {
            _options.markerClicked(this.id, JSON.parse(this.data));
        });
        _options.markers.push(marker);
    }

    this.addMarkers = (marker_options) => {
        marker_options.forEach(function (marker_option) {
            _this.addMarker(marker_option);
        });
    }

    this.removeMarker = (marker_id) => {
        _options.markers.forEach(function (marker) {
            if (marker.id === marker_id) {
                marker.setMap(null);
            }
        });
    }

    this.removeMarkers = (marker_ids) => {
        marker_ids.forEach(function (marker_id) {
            _this.removeMarker(marker_id);
        });
    }

    this.findMarker = (marker_id) => {
        return _options.markers.find(function (marker) {
            if (marker.id === marker_id) {
                return true;
            }
            return false;
        });
    }

    this.findMarkers = (marker_ids) => {
        return _options.markers.find(function (marker) {
            let check = false;
            marker_ids.forEach(function (marker_id) {
                if (marker.id === marker_id) {
                    check = true;
                }
            });
            return check;
        });
    }

    this.clearMarker = () => {
        _options.markers.forEach(function (marker) {
            marker.setMap(null);
        });
    }

    this.changeZoomLevel = (level) => {
        _options.map.setLevel(level);
    }

    this.moveMap = (latlng) => {
        let moveLatLon = new kakao.maps.LatLng(latlng.lat, latlng.lng);
        _options.map.panTo(moveLatLon);
    }

    this.moveMapByMarkerId = (marker_id) => {
        let pos = _this.findMarker(marker_id).getPosition();
        _options.map.panTo(pos);
    }

    async function getKey(type) {
        function apiGetKey(type) {
            const myHeaders = new Headers();
            myHeaders.append('Content-Type', 'application/json');
            let raw = JSON.stringify({type});
            const requestOptions = {
                method: 'POST',
                headers: myHeaders,
                body: raw,
            };
            if (_options.get_key_url == '/web/resources/assets/datas/getKey.json') {
                const response = fetch(`${_options.get_key_url}`);
                return response.then((res) => res.json());
            } else {
                const response = fetch(`${_options.get_key_url}`, requestOptions);
                return response.then((res) => res.json());
            }
        }

        let result;
        try {
            result = await apiGetKey(type);
            return result;
        } catch (error) {
            console.log(error);
        }
    }

    (() => {
        // Event functions
        return build.call(this);
    })()
}
