'use strict'
/**
 * AutoComplete,
 * pagination-inner class에 있는 개수를 파악하여 페이지네이션을 만들어 준다.
 *
 * @param {Object} options 페이지네이션 설정
 * @param {string || HTMLElement} options.container 컨테이너 엘리먼트 또는 셀렉터
 * @param {string} options.disabled_keyword 아이템들의 카운팅을 안하게 하는 class 키워드
 * @param {BigInteger} options.size 카운팅의 사이즈 (Ex:20이면 20개씩 한페이지)
 * @param {string} options.keyword 아이템들을 카운팅 하는 class 키워드
 * @param {string} options.align 페이지네이션 푸터 부분 Align 설정 (left,center,right)
 * */
const Pagination = function (options = {
    container: undefined,
    disabled_keyword: 'd-none',
    keyword: undefined,
    size: 10,
    align: 'left',
}) {
    this.options = {
        container: undefined,
        disabled_keyword: ['d-none'],
        keyword: undefined,
        size: 10,
        items: [],
        pages: 1,
        align: 'left',
        current: undefined,
    };
    if (options.container) {
        if (options.container.nodeType !== 1) {
            this.options.container = document.querySelector(options.container);
        } else {
            this.options.container = options.container;
        }
    } else {
        throw new Error(`element is not defined`);
    }
    this.options.disabled_keyword = options.hasOwnProperty('disabled_keyword') ? options.disabled_keyword : ['d-none'];
    this.options.keyword = options.hasOwnProperty('keyword') ? options.keyword : undefined;
    if (!this.options.keyword) {
        throw new Error('keyword is not defined');
    }
    this.options.size = options.hasOwnProperty('size') ? options.size : 10;
    this.options.align = options.hasOwnProperty('align') ? options.align : 'left';

    this.options.items = findItems(this.options.container, this.options.keyword, this.options.disabled_keyword);

    this.options.pages = Math.ceil(this.options.items.length / this.options.size);
    let _options = this.options;
    let _this = this;

    function build() {
        let FOOTER = createPaginationFooter(_options.pages);
        if (FOOTER) {
            _options.container.append(FOOTER);
        }
        _this.updateContainerUI();
        return _this;
    }

    function findItems(container, keyword, disabled_keyword) {
        let exclude_keyword = '';
        for (let i = 0; i < disabled_keyword.length; i++) {
            exclude_keyword += `:not(.${disabled_keyword[i]})`;
        }
        console.log('exclude_keyword', exclude_keyword);
        return container.querySelectorAll(`.pagination-inner .${keyword}${exclude_keyword}`);
    }

    function createPaginationFooter() {
        if (_options.pages <= 1) {
            return null;
        }
        let footer = document.createElement('div');
        footer.classList.add('pagination-footer');

        let pagination = document.createElement('ul');
        pagination.classList.add('pagination');
        if (_options.align === 'right') {
            pagination.classList.add('justify-content-end');
        } else if (_options.align === 'center') {
            pagination.classList.add('justify-content-center');
        } else {
            pagination.classList.add('justify-content-start');
        }

        //-1 is prev button
        //0 is next button
        let prev = createPaginationItem(-1, true, false);
        prev.addEventListener('click', pageButtonClickEventListener);
        pagination.append(prev);

        for (let i = 0; i < _options.pages; i++) {
            let page = createPaginationItem((i + 1), false, false);
            page.addEventListener('click', pageButtonClickEventListener);
            pagination.append(page);
            if (i === 0) {
                _options.current = page;
            }
        }

        //-1 is prev button
        //0 is next button
        let next = createPaginationItem(0, false, true);
        next.addEventListener('click', pageButtonClickEventListener);
        pagination.append(next);

        footer.append(pagination);
        return footer;
    }

    function createPaginationItem(index, isPrev = false, isNext = false) {
        function __buildPageItemInner(index) {
            if (isPrev) {
                return `<a class="page-link" href="#">Prev</a>`;
            }
            if (isNext) {
                return `<a class="page-link" href="#">Next</a>`;
            }
            return `<a class="page-link" href="#">${index}</a>`;
        }

        let li = document.createElement('li');
        li.classList.add('page-item');
        if (index == 1) {
            li.classList.add('active');
        }
        li.setAttribute('data-index', index);
        li.innerHTML = __buildPageItemInner(index);
        return li;
    }

    function pageButtonClickEventListener(event) {
        let index = this.dataset.index * 1;
        let current_item = _options.current;
        if (index === -1) {
            //Prev
            if (current_item.previousElementSibling.dataset.index * 1 != -1) {
                current_item.classList.remove('active');
                current_item.previousElementSibling.classList.add('active');
                _options.current = current_item.previousElementSibling;
            }
        } else if (index === 0) {
            //Next
            if (current_item.nextElementSibling.dataset.index * 1 != 0) {
                current_item.classList.remove('active');
                current_item.nextElementSibling.classList.add('active');
                _options.current = current_item.nextElementSibling;
            }
        } else {
            //Page
            current_item.classList.remove('active');
            this.classList.add('active');
            _options.current = this;
        }
        _this.updateContainerUI();
        event.stopPropagation();
        event.preventDefault();
    }

    this.updateSize = (update_size) => {
        _options.size = update_size;
        _options.pages = Math.ceil(_options.items.length / _options.size);
        _this.updateContainerUI(true);
    }

    this.updateKeyword = (update_keyword) => {
        _options.keyword = update_keyword;
        _options.items = findItems(_options.container, _options.keyword, _options.disabled_keyword);
        _options.pages = Math.ceil(_options.items.length / _options.size);
        _this.updateContainerUI(true);
        return true;
    }

    this.updateInnerUI = (is_update = false) => {
        if (_options.pages <= 1) {
            return false;
        }
        let current_item = _options.current;
        let index = current_item.dataset.index * 1;
        _options.items.forEach(function (item, count) {
            count += 1;
            if (((index - 1) * _options.size < count) && (count <= index * _options.size)) {
                item.style.display = 'block';
            } else {
                item.style.display = 'none';
            }
        });
        return true;
    }

    this.updateFooterUI = (is_update = false) => {
        let page_items = _options.container.querySelectorAll(`.pagination-footer .pagination .page-item`);
        if (_options.pages <= 1) {
            if (is_update) {
                let footer = _options.container.querySelector(`.pagination-footer`);
                footer.remove();
            }
            return false;
        }
        if (is_update) {
            let footer = _options.container.querySelector(`.pagination-footer`);
            footer.remove();
            let FOOTER = createPaginationFooter(_options.pages);
            if (FOOTER) {
                _options.container.append(FOOTER);
            }
            let current_item = _options.container.querySelector(`.pagination-footer .pagination .page-item[data-index="${1}"]`);
            let index = current_item.dataset.index * 1;
            let next =
                document.querySelector(`.pagination-container .pagination-footer .pagination .page-item[data-index="${0}"]`);
            let prev =
                document.querySelector(`.pagination-container .pagination-footer .pagination .page-item[data-index="${-1}"]`);

            if (index == 1) {
                if (!prev.classList.contains('disabled'))
                    prev.classList.add('disabled');
                if (next.classList.contains('disabled'))
                    next.classList.remove('disabled');
            } else if (index == (page_items.length - 2)) {
                if (prev.classList.contains('disabled'))
                    prev.classList.remove('disabled');
                if (!next.classList.contains('disabled'))
                    next.classList.add('disabled');
            } else {
                if (prev.classList.contains('disabled'))
                    prev.classList.remove('disabled');
                if (next.classList.contains('disabled'))
                    next.classList.remove('disabled');
            }
        } else {
            let current_item = _options.current;
            let index = current_item.dataset.index * 1;
            let next =
                document.querySelector(`.pagination-container .pagination-footer .pagination .page-item[data-index="${0}"]`);
            let prev =
                document.querySelector(`.pagination-container .pagination-footer .pagination .page-item[data-index="${-1}"]`);

            if (index == 1) {
                if (!prev.classList.contains('disabled'))
                    prev.classList.add('disabled');
                if (next.classList.contains('disabled'))
                    next.classList.remove('disabled');
            } else if (index == (page_items.length - 2)) {
                if (prev.classList.contains('disabled'))
                    prev.classList.remove('disabled');
                if (!next.classList.contains('disabled'))
                    next.classList.add('disabled');
            } else {
                if (prev.classList.contains('disabled'))
                    prev.classList.remove('disabled');
                if (next.classList.contains('disabled'))
                    next.classList.remove('disabled');
            }
        }
        return true;
    }

    this.updateContainerUI = (is_update = false) => {
        _this.updateFooterUI(is_update);
        _this.updateInnerUI(is_update);
    }

    (() => {
        // Event functions
        return build.call(this);
    })()
}
