steal.then('jsonix')
.then(function($) {
	
	$.Model('Page',
	/* @Static */
	{
		/**
		 * Get Page from Path or Id
		 * @param {Object} params path or id
		 */
		findOne: function(params, success, error) {
			if ('path' in params) {
				$.ajax('/system/weblounge/pages?details=true&path=' + params.path, {
					success: this.callback(['parseXMLPage', 'wrap', success])
				});
			} 
			else if ('id' in params) {
				$.ajax('/system/weblounge/pages/' + params.id, {
					success: this.callback(['parseXMLPage','wrap', success])
				});
			}
		},
		
		/**
		 * Get all Pages
		 */
		findAll: function(params, success, error) {
			$.ajax('/system/weblounge/pages/?sort=created-asc&limit=0&offset=0', {
				success: function(xml) {
					var json = Page.parseXML(xml);
					success(json.value.page);
				}
			});
		},
		
		/**
		 * Get Recent Pages
		 */
		findRecent: function(params, success, error) {
			$.ajax('/system/weblounge/pages/?sort=modified-desc&limit=8&offset=0', {
				success: function(xml) {
					var json = Page.parseXML(xml);
					success(json.value.page);
				}
			});
		},
		
		/**
		 * Get pages searched by string
		 */
		findBySearch: function(params, success, error) {
			$.ajax('/system/weblounge/pages/?searchterms=' + params.search + '&sort=modified-desc&limit=8&offset=0', {
				success: function(xml) {
					var json = Page.parseXML(xml);
					success(json.value.page);
				}
			});
		},
		
		/**
		 * Updates the specified page.
		 * @param {Object} params The page identifier
		 * @param {Page} page The page object
		 */
		update: function(params, page, success, error){
			if ('id' in params) {
				$.ajax({
					url: '/system/weblounge/pages/' + params.id,
					async: false,
					type: 'put',
					dataType: 'xml',
					data: {content : Page.parseJSON(page)}
				});
			}	
		},
		
		/**
		 * Creates a new page, either at the given path or at a random location and returns the REST url of the created resource.
		 * @param {Object} params The target path and optionally the page content
		 */
		create: function(params, success, error){
			if ('path' in params) {
				var data = {path : params.path};
				if('content' in params)
					data = {path : params.path, content : Page.parseJSON(params.content)}
				$.ajax({
					url: '/system/weblounge/pages/',
					type: 'post',
					dataType: 'xml',
					data: data,
					success: function(data, status, xhr){
						var url = xhr.getResponseHeader('Location');
						Page.findOne({id : url.substring(url.lastIndexOf('/') + 1)}, success);
					}

				});
			}	
		},
		
		/**
		 * Deletes the specified page.
		 * @param {Object} params The page identifier 
		 */
		destroy: function(params, success, error){
			if ('id' in params) {
				$.ajax({
					url: '/system/weblounge/pages/' + params.id,
					type: 'delete'
				});
			}
		},
		
		/**
		 * Converts XML to JSON
		 */
		parseXML: function(xml) {
			var unmarshaller = Editor.Jsonix.context().createUnmarshaller();
			return unmarshaller.unmarshalDocument(xml);
		},
		
		/**
		 * Converts XML to JSON
		 */
		parseXMLPage: function(xml) {
			var page = $(xml).find('page')[0];
			var unmarshaller = Editor.Jsonix.context().createUnmarshaller();
			return unmarshaller.unmarshalDocument(page);
		},
		
		/**
		 * Converts JSON to XML
		 */
		parseJSON: function(json) {
			var marshaller = Editor.Jsonix.context().createMarshaller();
			return marshaller.marshalString(json);
		}

	},
	/* @Prototype */
	{
		/**
		 * Return the specified Composer
		 * @param {String} composerId
		 */
	    getComposer: function(id) {
	    	var composer;
	    	$.each(this.value.body.composers, function(i, comp) {
	    		if(comp.id == id) {
	    			composer = comp;
	    			return false;
	    		}
    		});
	    	return composer;
	    },
	    
	    /**
	     * Return the specified Composer Index
	     * @param {String} composerId
	     */
	    getComposerIndex: function(id) {
	    	var index = -1;
	    	$.each(this.value.body.composers, function(i, composer) {
	    		if(composer.id == id) {
	    			index = i;
	    			return false;
	    		};
    		});
	    	return index;
	    },
	    
	    deletePagelet: function(composerId, index){
	    	var composer = this.getComposer(composerId);
	    	composer.pagelets.splice(index, 1);
	    	Page.update({id:this.value.id}, this);
	    },
 	    
	    /**
	     * Return the specified Pagelet
	     * @param {String} composerId
	     * @param {int} index
	     */
	    getPagelet: function(composerId, index) {
	    	var composer = this.getComposer(composerId);
	    	return composer.pagelets[index];
	    },
	    
	    /**
	     * Create an empty composer element
	     * @param {String} composerId Composer to create
	     */
	    createComposer: function(composerId) {
	    	if($.isEmptyObject(this.value.body.composers)) {
	    		this.value.body.composers = new Array();
	    	}
	    	
	    	if(this.getComposerIndex(composerId) != -1) return;
	    	this.value.body.composers.push({id: composerId});
	    },
	    
	    /**
	     * Changes current Composer with new one and Update in Repository
	     * @param {String} composerId Composer to change
	     * @param {Object} newComposer New Composer Object
	     */
	    updateComposer: function(composerId, newComposer) {
	    	var index = this.getComposerIndex(composerId);
	    	this.value.body.composers[index].pagelets = newComposer;
	    	Page.update({id:this.value.id}, this);
	    },
	    
	    /**
	     * Return Pagelet for Pagelet-Editor with current and original Language
	     * @param {String} composerId
	     * @param {int} index
	     * @param {String} language
	     */
	    getEditorPagelet: function(composerId, index, language) {
	    	var pagelet = this.getPagelet(composerId, index);
	    	var copyPagelet = jQuery.extend(true, {}, pagelet);

			$.each(copyPagelet.locale, function(i, locale) {
				if(locale.language == language) {
					copyPagelet.locale.current = locale;
				}
				if(locale.original == true) {
					copyPagelet.locale.original = locale;
				}
			});
			return copyPagelet;
	    },
	    
	    /**
	     * Insert Pagelet to the specified position in the composer, this removes the current pagelet at that position
	     * @param {Object} pagelet The pagelet to insert
	     * @param {String} composerId The parent composer id 
	     * @param {int} index The pagelet index to insert
	     */
	    insertPagelet: function(pagelet, composerId, index) {
	    	delete pagelet.locale.current;
	    	delete pagelet.locale.original;
	    	this.value.body.composers[this.getComposerIndex(composerId)].pagelets[index] = pagelet;
	    	Page.update({id:this.value.id}, this);
	    },
	    
	    /**
	     * Update the page with the new data from the creation dialog.
	     * @param {Object} creationData The data from the creation dialog
	     * @param {String} language The languageId
	     */
	    saveCreationData: function(creationData, language) {
			if($.isEmptyObject(this.value.head.metadata)) {
				this.value.head.metadata = {};
			}
			if($.isEmptyObject(this.value.head.metadata.title)) {
				this.value.head.metadata.title = {};
			}
			if($.isEmptyObject(this.value.head.metadata.description)) {
				this.value.head.metadata.description = {};
			}
			if($.isEmptyObject(this.value.head.metadata.subject)) {
				this.value.head.metadata.subject = [];
			}
			
			this.value.head.template = creationData.layout;
			
			this.value.head.metadata.title[language] = creationData.title;
			this.value.head.metadata.description[language] = creationData.description;
			
			// Filter out empty values
			this.value.head.metadata.subject = creationData.tags.split(/,\s*/).filter(function(value) { 
				return value != ''; 
			});
			
			Page.update({id:this.value.id}, this);
		}
	    
	});

});
